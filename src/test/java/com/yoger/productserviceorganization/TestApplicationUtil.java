package com.yoger.productserviceorganization;

import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class TestApplicationUtil {
    private final WebTestClient webTestClient;

    TestApplicationUtil(WebTestClient webClient) {
        this.webTestClient = webClient;
    }

    MultipartBodyBuilder getTestBodyBuilder() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("name", "Test Product");
        builder.part("description", "This is a test product description");
        builder.part("image", new ClassPathResource("test-image.jpeg"))
                .filename("test-image.jpeg")
                .contentType(MediaType.IMAGE_JPEG);
        builder.part("thumbnailImage", new ClassPathResource("test-thumbnail.jpeg"))
                .filename("test-thumbnail.jpeg")
                .contentType(MediaType.IMAGE_JPEG);
        builder.part("creatorName", "Test Creator");  // 제작자 이름 추가
        return builder;
    }

    List<SimpleDemoProductResponseDTO> getSimpleDemoTestProducts() {
        return webTestClient.get()
                .uri("/api/products/demo")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SimpleDemoProductResponseDTO.class)  // body를 List로 매핑
                .returnResult()
                .getResponseBody();
    }

    DemoProductResponseDTO makeDemoTestProduct(Long creatorId, MultipartBodyBuilder builder) {
        return webTestClient.post()
                .uri("/api/products/demo")
                .header("User-Id", String.valueOf(creatorId))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DemoProductResponseDTO.class)  // DTO로 응답 본문을 매핑
                .returnResult()
                .getResponseBody();
    }

    DemoProductResponseDTO updateDemoTestProduct(Long id, Long creatorId, MultipartBodyBuilder builder) {
        return webTestClient.patch()
                .uri("/api/products/demo/" + id)
                .header("User-Id", String.valueOf(creatorId))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(DemoProductResponseDTO.class)  // DTO로 응답 본문을 매핑
                .returnResult()
                .getResponseBody();
    }

    void deleteDemoTestProduct(Long productId, Long creatorId) {
        webTestClient.delete()
                .uri("/api/products/demo/" + productId)
                .header("User-Id", String.valueOf(creatorId))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }
}
