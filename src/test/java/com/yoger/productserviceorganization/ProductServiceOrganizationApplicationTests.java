package com.yoger.productserviceorganization;

import static org.assertj.core.api.Assertions.assertThat;

import com.yoger.productserviceorganization.proruct.config.AwsProperties;
import com.yoger.productserviceorganization.proruct.domain.model.ProductState;
import com.yoger.productserviceorganization.proruct.dto.response.DemoProductResponseDTO;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = LocalStackS3Config.class
)
@ActiveProfiles("integration")
@Testcontainers
class ProductServiceOrganizationApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private S3Client s3TestClient;

    @Autowired
    private AwsProperties awsProperties;

    @BeforeEach
    public void setUp() {
        // 버킷을 미리 생성
        s3TestClient.createBucket(CreateBucketRequest.builder().bucket(awsProperties.bucket()).build());
    }

    private MultipartBodyBuilder getTestBodyBuilder() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("name", "Test Product");
        builder.part("description", "This is a test product description");
        builder.part("image", new ClassPathResource("test-image.jpeg"))
                .filename("test-image.jpeg")
                .contentType(MediaType.IMAGE_JPEG);
        return builder;
    }

    private DemoProductResponseDTO makeDemoTestProduct(MultipartBodyBuilder builder) {
        return webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DemoProductResponseDTO.class)  // DTO로 응답 본문을 매핑
                .returnResult()
                .getResponseBody();
    }

    @Test
    void whenPostRequestThenProductCreated() throws IOException {
        MultipartBodyBuilder builder = getTestBodyBuilder();

        DemoProductResponseDTO demoProductResponseDTO = makeDemoTestProduct(builder);

        String expectedImageUrlPattern = String.format(
                "https://%s\\.s3\\.%s\\.amazonaws\\.com/[a-f0-9\\-]+_test-image\\.jpeg",
                awsProperties.bucket(),
                awsProperties.region()
        );

        assertThat(demoProductResponseDTO.name()).isEqualTo("Test Product");
        assertThat(demoProductResponseDTO.description()).isEqualTo("This is a test product description");
        assertThat(demoProductResponseDTO.imageUrl()).matches(expectedImageUrlPattern);
        assertThat(demoProductResponseDTO.state()).isEqualTo(ProductState.DEMO);
    }

    @Test
    void whenDemoGetRequestThenDemoListReturned() {
        MultipartBodyBuilder builder = getTestBodyBuilder();

        DemoProductResponseDTO demoProductResponseDTO = makeDemoTestProduct(builder);

        List<DemoProductResponseDTO> demoProductResponseDTOs = webTestClient.get()
                .uri("/api/products/demo")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DemoProductResponseDTO.class)  // body를 List로 매핑
                .returnResult()
                .getResponseBody();

        assertThat(demoProductResponseDTOs.size()).isEqualTo(1);
        assertThat(demoProductResponseDTOs.get(0)).isEqualTo(demoProductResponseDTO);
    }
}
