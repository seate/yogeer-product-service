package com.yoger.productserviceorganization.product.persistence;

import com.yoger.productserviceorganization.product.domain.model.ProductState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByState(ProductState state);
}
