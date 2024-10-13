package com.yoger.productserviceorganization.product.adapters.persistence.jpa;

import com.yoger.productserviceorganization.product.domain.model.ProductState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByState(ProductState state);
}
