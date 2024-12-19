package com.yoger.productserviceorganization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import com.yoger.productserviceorganization.product.config.AwsProperties;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = LocalStackS3Config.class
)
@ActiveProfiles({"integration", "aws"})
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceOrganizationApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private S3Client s3TestClient;

    @Autowired
    private AwsProperties awsProperties;

    private TestApplicationUtil applicationUtil;

    @BeforeAll
    public void setUp() {
        // 버킷을 미리 생성
        s3TestClient.createBucket(CreateBucketRequest.builder().bucket(awsProperties.bucket()).build());
        this.applicationUtil = new TestApplicationUtil(webTestClient);
    }

    @Test
    void whenPostRequestThenProductCreated() {
        // Given
        MultipartBodyBuilder builder = applicationUtil.getTestBodyBuilder();

        // When
        DemoProductResponseDTO demoProductResponseDTO = applicationUtil.makeDemoTestProduct(1L, builder);

        // Then
        String expectedImageUrlPattern = String.format(
                "https://%s\\.s3\\.%s\\.amazonaws\\.com/[a-f0-9\\-]+_test-image\\.jpeg",
                awsProperties.bucket(),
                awsProperties.region()
        );

        // 이미지 URL에 대한 별도 검증
        assertThat(demoProductResponseDTO.imageUrl()).matches(expectedImageUrlPattern);

        // 나머지 필드 검증
        assertThat(demoProductResponseDTO)
                .extracting(
                        DemoProductResponseDTO::name,
                        DemoProductResponseDTO::description,
                        DemoProductResponseDTO::creatorId,
                        DemoProductResponseDTO::creatorName,
                        DemoProductResponseDTO::state
                )
                .containsExactly(
                        "Test Product",
                        "This is a test product description",
                        1L,
                        "Test Creator",
                        ProductState.DEMO
                );
    }

    @Test
    void whenDemoGetRequestThenDemoListReturned() {
        // Given
        MultipartBodyBuilder builder = applicationUtil.getTestBodyBuilder();

        // First, create a demo product
        DemoProductResponseDTO demoProductResponseDTO = applicationUtil.makeDemoTestProduct(1L, builder);

        // When
        List<SimpleDemoProductResponseDTO> demoProductResponseDTOs = applicationUtil.getSimpleDemoTestProducts();

        // Then
        assertThat(demoProductResponseDTOs)
                .hasSize(1)
                .extracting(
                        SimpleDemoProductResponseDTO::id,
                        SimpleDemoProductResponseDTO::name,
                        SimpleDemoProductResponseDTO::creatorName,
                        SimpleDemoProductResponseDTO::state
                )
                .containsExactly(
                        tuple(
                                demoProductResponseDTO.id(),
                                demoProductResponseDTO.name(),
                                demoProductResponseDTO.creatorName(),
                                demoProductResponseDTO.state()
                        )
                );
    }

    @Test
    void whenDemoUpdatedThenUpdatedDemoProductReturned() {
        // Given
        MultipartBodyBuilder builder = applicationUtil.getTestBodyBuilder();

        // First, create a demo product
        DemoProductResponseDTO originDemoProductResponseDTO = applicationUtil.makeDemoTestProduct(1L, builder);

        MultipartBodyBuilder updateBuilder = new MultipartBodyBuilder();
        updateBuilder.part("image", new ClassPathResource("updated-image.jpeg"))
                .filename("updated-image.jpeg")
                .contentType(MediaType.IMAGE_JPEG);

        // When
        DemoProductResponseDTO updatedDemoProductResponseDTO =
                applicationUtil.updateDemoTestProduct(
                        originDemoProductResponseDTO.id(),
                        originDemoProductResponseDTO.creatorId(),
                        updateBuilder
                );

        // Then
        String expectedImageUrlPattern = String.format(
                "https://%s\\.s3\\.%s\\.amazonaws\\.com/[a-f0-9\\-]+_updated-image\\.jpeg",
                awsProperties.bucket(),
                awsProperties.region()
        );

        assertThat(originDemoProductResponseDTO.name()).isEqualTo(updatedDemoProductResponseDTO.name());
        assertThat(originDemoProductResponseDTO.description()).isEqualTo(updatedDemoProductResponseDTO.description());

        assertThat(updatedDemoProductResponseDTO.imageUrl()).matches(expectedImageUrlPattern);
    }

    @Test
    void whenDemoDeletedThenProductShouldBeRemoved() {
        // Given
        MultipartBodyBuilder builder = applicationUtil.getTestBodyBuilder();

        // First, create a demo product
        DemoProductResponseDTO demoProductResponseDTO = applicationUtil.makeDemoTestProduct(1L, builder);

        // When: Delete the created product
        applicationUtil.deleteDemoTestProduct(demoProductResponseDTO.id(), demoProductResponseDTO.creatorId());

        // Then: Fetch the list again and ensure the product is deleted
        List<SimpleDemoProductResponseDTO> demoProductResponseDTOs = applicationUtil.getSimpleDemoTestProducts();

        assertThat(demoProductResponseDTOs)
                .extracting(
                        SimpleDemoProductResponseDTO::id,
                        SimpleDemoProductResponseDTO::name,
                        SimpleDemoProductResponseDTO::creatorName,
                        SimpleDemoProductResponseDTO::state
                )
                .doesNotContain(
                        tuple(
                                demoProductResponseDTO.id(),
                                demoProductResponseDTO.name(),
                                demoProductResponseDTO.creatorName(),
                                demoProductResponseDTO.state()
                        )
                );
    }
}
