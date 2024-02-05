package org.kakaoshare.backend.domain.product.repository;

import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductThumbnailRepository extends JpaRepository<ProductThumbnail, Long> {
}
