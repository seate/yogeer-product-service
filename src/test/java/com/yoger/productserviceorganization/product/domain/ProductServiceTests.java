package com.yoger.productserviceorganization.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.yoger.productserviceorganization.proruct.domain.ProductService;
import com.yoger.productserviceorganization.proruct.domain.model.ProductState;
import com.yoger.productserviceorganization.proruct.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;
import com.yoger.productserviceorganization.proruct.persistence.ProductRepository;
import java.util.Arrays;
import java.util.List;
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

    @Test
    void viewSellableProducts_ReturnsSellableProducts() {
        ProductEntity sellableProduct = ProductEntity.of(
                "유효한 상품",
                "[{\"quantity\": 100, \"price\": 10000}, {\"quantity\": 1000, \"price\": 8500}]",
                "상품에 대한 설명입니다.",
                "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                ProductState.SELLABLE
        );
        ProductEntity demoProduct = ProductEntity.of(
                "유효한 데모 상품",
                null,
                "상품에 대한 설명입니다.",
                "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                ProductState.DEMO
        );
        ProductEntity saleEndedProduct = ProductEntity.of(
                "유효한 판매 끝난 상품",
                "[{\"quantity\": 100, \"price\": 10000}, {\"quantity\": 1000, \"price\": 8500}]",
                "상품에 대한 설명입니다.",
                "https://my-bucket.s3.us-west-1.amazonaws.com/myimage.jpg",
                ProductState.SALE_ENDED
        );
        given(productRepository.findAll()).willReturn(Arrays.asList(sellableProduct, demoProduct, saleEndedProduct));

        // When
        List<SellableProductResponseDTO> result = productService.viewSellableProducts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("유효한 상품");
        assertThat(result.get(0).state()).isEqualTo(ProductState.SELLABLE);
        assertThat(result.get(0).priceByQuantity()).isEqualTo("[{\"quantity\": 100, \"price\": 10000}, {\"quantity\": 1000, \"price\": 8500}]");
    }
}
