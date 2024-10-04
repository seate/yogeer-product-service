package com.yoger.productserviceorganization.proruct.mapper;

import com.yoger.productserviceorganization.proruct.domain.model.ProductState;
import com.yoger.productserviceorganization.proruct.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;

public class ProductMapper {
    public static ProductEntity toPersistenceFrom(DemoProductRequestDTO productRequestDTO, String imageUrl) {
        return ProductEntity.of(
                productRequestDTO.name(),
                null, // 나중에 설정
                productRequestDTO.description(),
                imageUrl,
                ProductState.DEMO
        );
    }
}
