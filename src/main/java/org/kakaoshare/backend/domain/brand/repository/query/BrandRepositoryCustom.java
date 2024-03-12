package org.kakaoshare.backend.domain.brand.repository.query;

import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandRepositoryCustom {
    Page<SimpleBrandDto> findAllSimpleBrandByCategoryId(Long categoryId, Pageable pageable);
}
