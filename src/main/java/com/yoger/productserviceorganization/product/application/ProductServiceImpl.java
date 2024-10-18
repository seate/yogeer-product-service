package com.yoger.productserviceorganization.product.application;

import com.yoger.productserviceorganization.product.adapters.web.dto.request.UpdatedDemoProductRequestDTO;
import com.yoger.productserviceorganization.product.domain.port.ProductRepository;
import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.domain.port.ImageStorageService;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleSellableProductResponseDTO;
import com.yoger.productserviceorganization.product.mapper.ProductMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageStorageService imageStorageService;

    public List<SimpleSellableProductResponseDTO> findSimpleSellableProducts() {
        return productRepository.findByState(ProductState.SELLABLE).stream()
                .map(this::mapToSellableDTO)
                .toList();
    }

    private SimpleSellableProductResponseDTO mapToSellableDTO(Product product) {
        return SimpleSellableProductResponseDTO.from(product);
    }

    @Transactional
    public DemoProductResponseDTO saveDemoProduct(@Valid DemoProductRequestDTO demoProductRequestDTO) {
        String imageUrl = imageStorageService.uploadImage(demoProductRequestDTO.image());
        String thumbnailImageUrl = imageStorageService.uploadImage(demoProductRequestDTO.thumbnailImage());
        Product product = ProductMapper.toDomainFrom(demoProductRequestDTO, imageUrl, thumbnailImageUrl);
        return DemoProductResponseDTO.from(productRepository.save(product));
    }

    public List<SimpleDemoProductResponseDTO> findSimpleDemoProducts() {
        return productRepository.findByState(ProductState.DEMO).stream()
                .map(this::mapToDemoDTO)
                .toList();
    }

    private SimpleDemoProductResponseDTO mapToDemoDTO(Product product) {
        return SimpleDemoProductResponseDTO.from(product);
    }

    public SellableProductResponseDTO findSellableProduct(Long productId) {
        Product product = productRepository.findById(productId);
        product.validateUnexpectedState(ProductState.SELLABLE);
        return SellableProductResponseDTO.from(product);
    }

    public DemoProductResponseDTO findDemoProduct(Long productId) {
        Product product = productRepository.findById(productId);
        product.validateUnexpectedState(ProductState.DEMO);
        return DemoProductResponseDTO.from(product);
    }

    @Transactional
    public DemoProductResponseDTO updateDemoProduct(Long productId,
                                                    UpdatedDemoProductRequestDTO updatedDemoProductRequestDTO) {
        Product product = productRepository.findById(productId);
        product.validateUnexpectedState(ProductState.DEMO);

        if (updatedDemoProductRequestDTO.image() != null && !updatedDemoProductRequestDTO.image().isEmpty()) {
            String newImageUrl = imageStorageService.uploadImage(updatedDemoProductRequestDTO.image());
            imageStorageService.deleteImage(product.getImageUrl());
            product.updateImageUrl(newImageUrl);
        }

        if (updatedDemoProductRequestDTO.thumbnailImage() != null && !updatedDemoProductRequestDTO.thumbnailImage()
                .isEmpty()) {
            String newThumbnailImageUrl = imageStorageService.uploadImage(
                    updatedDemoProductRequestDTO.thumbnailImage());
            imageStorageService.deleteImage(product.getThumbnailImageUrl());
            product.updateThumbnailImageUrl(newThumbnailImageUrl);
        }

        if (updatedDemoProductRequestDTO.name() != null) {
            product.updateName(updatedDemoProductRequestDTO.name());
        }

        if (updatedDemoProductRequestDTO.description() != null) {
            product.updateDescription(updatedDemoProductRequestDTO.description());
        }

        return DemoProductResponseDTO.from(product);
    }
}
