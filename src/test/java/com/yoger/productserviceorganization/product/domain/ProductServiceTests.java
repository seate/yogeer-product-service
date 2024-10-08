package com.yoger.productserviceorganization.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;
import com.yoger.productserviceorganization.product.persistence.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductService productService; // 테스트할 대상

    @Mock
    private ProductRepository productRepository; // 목 객체

    private List<PriceByQuantity> priceByQuantities;

    @BeforeEach
    void setUp() {
        priceByQuantities = List.of(
                new PriceByQuantity(100, 10000)
                , new PriceByQuantity(1000, 8500)
                , new PriceByQuantity(10000, 7500)
        );
    }

    @Test
    void viewSellableProducts_ReturnsSellableProducts() {
        ProductEntity sellableProduct = ProductEntity.of(
                "유효한 상품",
                priceByQuantities,
                "상품에 대한 설명입니다.",
                "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                ProductState.SELLABLE
        );
        given(productRepository.findByState(ProductState.SELLABLE)).willReturn(List.of(sellableProduct));

        // When
        List<SellableProductResponseDTO> result = productService.viewSellableProducts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("유효한 상품");
        assertThat(result.get(0).state()).isEqualTo(ProductState.SELLABLE);
        assertThat(result.get(0).priceByQuantities()).isEqualTo(priceByQuantities);
    }
}
