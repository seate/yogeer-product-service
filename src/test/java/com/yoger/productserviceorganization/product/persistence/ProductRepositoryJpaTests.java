package com.yoger.productserviceorganization.product.persistence;

import static org.assertj.core.api.Assertions.*;

import com.yoger.productserviceorganization.proruct.config.DataConfig;
import com.yoger.productserviceorganization.proruct.domain.model.ProductState;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;
import com.yoger.productserviceorganization.proruct.persistence.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class ProductRepositoryJpaTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품들이 정상적으로 저장되었는 지 테스트")
    void findAllProducts() {
        ProductEntity productEntity1 = ProductEntity.of(
                "유효한상품이름1",
                "[{\"quantity\": 100, \"price\": 10000}, {\"quantity\": 1000, \"price\": 8500}]",
                "상품에 대한 설명입니다.",
                "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                ProductState.SELLABLE
        );
        ProductEntity productEntity2 = ProductEntity.of(
                "유효한상품이름2",
                "[{\"quantity\": 100, \"price\": 10000}, {\"quantity\": 1000, \"price\": 8500}]",
                "상품에 대한 설명입니다.",
                "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                ProductState.SELLABLE
        );
        productRepository.save(productEntity1);
        productRepository.save(productEntity2);

        List<ProductEntity> actualProducts = productRepository.findAll();

        assertThat(actualProducts.parallelStream()
                .filter(productEntity -> productEntity.getName().equals(productEntity1.getName()) || productEntity.getName()
                        .equals(productEntity2.getName()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @ParameterizedTest
    @MethodSource("invalidProductParameters")
    @DisplayName("유효성 검증 실패 테스트 - 다양한 경우")
    void productValidationFailTest(String name, String description, String imageUrl, ProductState state, String expectedMessage) {
        ProductEntity productEntity = ProductEntity.of(
                name,
                "[{\"quantity\": 100, \"price\": 10000}, {\"quantity\": 1000, \"price\": 8500}]",
                description,
                imageUrl,
                state
        );

        assertThatThrownBy(() -> productRepository.save(productEntity))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(expectedMessage);
    }

    // 유효성 실패 케이스들을 제공하는 메서드
    private static Stream<Arguments> invalidProductParameters() {
        return Stream.of(
                // name 유효성 검증 실패 케이스
                Arguments.of("짧", "상품에 대한 설명입니다.", "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                        ProductState.SELLABLE, "상품 이름은 2글자 이상 50글자 이하만 가능합니다."),
                Arguments.of("이름이 너무너무 길어요".repeat(10), "상품에 대한 설명입니다.",
                        "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg", ProductState.SELLABLE,
                        "상품 이름은 2글자 이상 50글자 이하만 가능합니다."),
                Arguments.of("이름에 허용하지 않은 특수부호 #", "상품에 대한 설명입니다.",
                        "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg", ProductState.SELLABLE,
                        "상품 이름은 한글, 영어, 숫자, '-', '_' 만 사용할 수 있습니다."),

                // description 유효성 검증 실패 케이스
                Arguments.of("정상 이름", "짧", "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                        ProductState.SELLABLE, "상품 상세 설명은 10글자 이상 500글자 이하만 가능합니다."),
                Arguments.of("정상 이름", "상품 설명이 너무 길어요".repeat(100),
                        "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg", ProductState.SELLABLE,
                        "상품 상세 설명은 10글자 이상 500글자 이하만 가능합니다."),

                // imageUrl 유효성 검증 실패 케이스
                Arguments.of("정상 이름", "상품에 대한 설명입니다.", "https://wrong-url.com/myimage.jpg", ProductState.SELLABLE,
                        "유효한 S3 URL 형식이어야 합니다."),

                // state 유효성 검증 실패 케이스
                Arguments.of("정상 이름", "상품에 대한 설명입니다.", "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg", null,
                        "상품의 상태를 정해주세요.")
        );
    }
}
