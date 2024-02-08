package org.kakaoshare.backend.domain.product.repository;

import org.kakaoshare.backend.domain.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
