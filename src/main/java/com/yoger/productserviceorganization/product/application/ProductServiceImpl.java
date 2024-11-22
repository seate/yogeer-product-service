package com.yoger.productserviceorganization.product.application;

import com.yoger.productserviceorganization.product.adapters.web.dto.request.UpdatedDemoProductRequestDTO;
import com.yoger.productserviceorganization.product.domain.exception.InvalidProductException;
import com.yoger.productserviceorganization.product.domain.exception.InvalidStockException;
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
    public DemoProductResponseDTO updateDemoProduct(
            Long productId,
            Long creatorId,
            UpdatedDemoProductRequestDTO updatedDemoProductRequestDTO
    ) {
        Product product = productRepository.findByIdWithLock(productId);
        product.validateUnexpectedState(ProductState.DEMO);
        product.validateCreatorId(creatorId);

        String updatedProductName = product.getName();
        if (updatedDemoProductRequestDTO.name() != null) {
            updatedProductName = updatedDemoProductRequestDTO.name();
        }
        String updatedProductDescription = product.getDescription();
        if (updatedDemoProductRequestDTO.description() != null) {
            updatedProductDescription = updatedDemoProductRequestDTO.description();
        }
        String updatedImageUrl = imageStorageService.updateImage(
                updatedDemoProductRequestDTO.image(),
                product.getImageUrl()
        );
        String updatedThumbnailImageUrl = imageStorageService.updateImage(
                updatedDemoProductRequestDTO.thumbnailImage(),
                product.getThumbnailImageUrl()
        );
        Product updatedProduct = Product.updatedDemoProduct(
                product,
                updatedProductName,
                updatedProductDescription,
                updatedImageUrl,
                updatedThumbnailImageUrl
        );

        Product savedProduct = productRepository.save(updatedProduct);
        return DemoProductResponseDTO.from(savedProduct);
    }

    @Transactional
    public void deleteDemoProduct(Long productId, Long creatorId) {
        Product product = productRepository.findByIdWithLock(productId);
        product.validateUnexpectedState(ProductState.DEMO);
        product.validateCreatorId(creatorId);

        imageStorageService.deleteImage(product.getImageUrl());
        imageStorageService.deleteImage(product.getThumbnailImageUrl());
        productRepository.deleteById(productId);
    }

    @Transactional
    public void changeSellableProductStock(Long productId, Integer quantity) {
        int flag = productRepository.updateStock(productId, quantity);
        if(flag == 0) {
            if(productRepository.existsById(productId)) {
                throw new InvalidProductException("존재하지 않는 상품에 대한 재고 변경 요청입니다.");
            }
            throw new InvalidStockException("상품의 재고가 부족합니다.");
        }
    }
}
