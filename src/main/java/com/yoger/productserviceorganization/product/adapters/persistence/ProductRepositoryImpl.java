package com.yoger.productserviceorganization.product.adapters.persistence;

import com.yoger.productserviceorganization.product.adapters.persistence.jpa.JpaProductRepository;
import com.yoger.productserviceorganization.product.adapters.persistence.jpa.ProductEntity;
import com.yoger.productserviceorganization.product.domain.port.ProductRepository;
import com.yoger.productserviceorganization.product.domain.exception.ProductNotFoundException;
import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.mapper.ProductMapper;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private static final String PRODUCT_ENTITY_CACHE = "productEntity";
    private static final String PRODUCT_ENTITY_CACHE_BY_STATE = "productEntitiesByState";
    private static final String PRODUCT_ENTITY_CACHE_ALL = "allProductEntities";

    private final JpaProductRepository jpaProductRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Product findById(Long productId) {
        String cacheKey = PRODUCT_ENTITY_CACHE + " : " + productId;
        ProductEntity cachedEntity = (ProductEntity) redisTemplate.opsForValue().get(cacheKey);
        if (cachedEntity != null) {
            return ProductMapper.toDomainFrom(cachedEntity);
        }
        ProductEntity productEntity = jpaProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        redisTemplate.opsForValue().set(cacheKey, productEntity, Duration.ofMinutes(5));
        return ProductMapper.toDomainFrom(productEntity);
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = ProductMapper.toEntityFrom(product);
        ProductEntity savedEntity = jpaProductRepository.save(productEntity);
        String cacheKey = PRODUCT_ENTITY_CACHE + " : " + savedEntity.getId();
        redisTemplate.opsForValue().set(cacheKey, savedEntity, Duration.ofMinutes(5));

        evictCacheForAllProducts();
        evictCacheForState(savedEntity.getState());

        return ProductMapper.toDomainFrom(savedEntity);
    }

    @Override
    public List<Product> findByState(ProductState state) {
        String cacheKey = PRODUCT_ENTITY_CACHE_BY_STATE + " : " + state.name();
        List<ProductEntity> cachedEntities = (List<ProductEntity>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedEntities != null) {
            return cachedEntities.stream()
                    .map(ProductMapper::toDomainFrom)
                    .collect(Collectors.toList());
        }

        List<ProductEntity> productEntities = jpaProductRepository.findByState(state);

        redisTemplate.opsForValue().set(cacheKey, productEntities, Duration.ofMinutes(5));
        return productEntities.stream()
                .map(ProductMapper::toDomainFrom)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAll() {
        String cacheKey = PRODUCT_ENTITY_CACHE_ALL + " : allProducts";
        List<ProductEntity> cachedEntities = (List<ProductEntity>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedEntities != null) {
            return cachedEntities.stream()
                    .map(ProductMapper::toDomainFrom)
                    .collect(Collectors.toList());
        }

        List<ProductEntity> productEntities = jpaProductRepository.findAll();

        redisTemplate.opsForValue().set(cacheKey, productEntities, Duration.ofMinutes(5));

        return productEntities.stream()
                .map(ProductMapper::toDomainFrom)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long productId) {
        jpaProductRepository.deleteById(productId);

        String cacheKey = PRODUCT_ENTITY_CACHE + " : " + productId;
        redisTemplate.delete(cacheKey);

        evictCacheForAllProducts();
        evictAllStateCaches();
    }

    @Override
    public Product findByIdWithLock(Long productId) {
        ProductEntity productEntity = jpaProductRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductMapper.toDomainFrom(productEntity);
    }

    @Override
    public Integer updateStock(Long productId, Integer quantity) {
        Integer result = jpaProductRepository.updateStock(productId, quantity);
        if (result > 0) {
            String cacheKey = PRODUCT_ENTITY_CACHE + " : " + productId;
            ProductEntity updatedEntity = jpaProductRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            redisTemplate.opsForValue().set(cacheKey, updatedEntity, Duration.ofMinutes(5));
            // No need to evict all caches since stock update may not affect other cached data
        }
        return result;
    }

    @Override
    public Boolean existsById(Long productId) {
        return jpaProductRepository.existsById(productId);
    }

    @Override
    public List<Product> findByCreatorId(Long creatorId) {
        return jpaProductRepository.findAllByCreatorId(creatorId)
                .stream()
                .map(ProductMapper::toDomainFrom)
                .toList();
    }

    private void evictCacheForState(ProductState state) {
        String cacheKey = PRODUCT_ENTITY_CACHE_BY_STATE + " : " + state.name();
        redisTemplate.delete(cacheKey);
    }

    private void evictCacheForAllProducts() {
        String pattern = PRODUCT_ENTITY_CACHE_ALL + " : *";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private void evictAllStateCaches() {
        String pattern = PRODUCT_ENTITY_CACHE_BY_STATE + " : *";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
