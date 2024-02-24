package org.kakaoshare.backend.domain.product.repository.query;

import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<SimpleProductDto> findAllByCategoryId(Long categoryId, Pageable pageable);
}
