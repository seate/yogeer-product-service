package com.yoger.productserviceorganization.product.domain;

import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.product.mapper.ProductMapper;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;
import com.yoger.productserviceorganization.product.persistence.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    public List<SellableProductResponseDTO> viewSellableProducts() {
        return productRepository.findByState(ProductState.SELLABLE).stream()
                .map(this::mapToSellableDTO)
                .toList();
    }

    private SellableProductResponseDTO mapToSellableDTO(ProductEntity productEntity) {
        return SellableProductResponseDTO.from(productEntity);
    }

    public DemoProductResponseDTO saveDemoProduct(@Valid DemoProductRequestDTO demoProductRequestDTO) {
        String imageUrl = s3Service.uploadImage(demoProductRequestDTO.image());
        String thumbnailImageUrl = s3Service.uploadImage(demoProductRequestDTO.thumbnailImage());
        ProductEntity productEntity = ProductMapper.toPersistenceFrom(demoProductRequestDTO, imageUrl, thumbnailImageUrl);
        productRepository.save(productEntity);
        return DemoProductResponseDTO.from(productEntity);
    }

    public List<DemoProductResponseDTO> viewDemoProducts() {
        return productRepository.findByState(ProductState.DEMO).stream()
                .map(this::mapToDemoDTO)
                .toList();
    }

    private DemoProductResponseDTO mapToDemoDTO(ProductEntity productEntity) {
        return DemoProductResponseDTO.from(productEntity);
    }
}
