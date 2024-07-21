package org.kakaoshare.backend.domain.product.repository;

import org.kakaoshare.backend.domain.hashtag.entity.ProductHashtag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductHashtagRepository extends JpaRepository<ProductHashtag, Long> {
}
