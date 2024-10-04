package com.yoger.productserviceorganization.proruct.domain;

import com.yoger.productserviceorganization.proruct.domain.model.ProductState;
import com.yoger.productserviceorganization.proruct.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.proruct.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.proruct.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.proruct.mapper.ProductMapper;
import com.yoger.productserviceorganization.proruct.persistence.ProductEntity;
import com.yoger.productserviceorganization.proruct.persistence.ProductRepository;
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
        ProductEntity productEntity = ProductMapper.toPersistenceFrom(demoProductRequestDTO, imageUrl);
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
