package org.kakaoshare.backend.domain.brand.repository.query;

import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {
    Page<SimpleBrandDto> findAllSimpleBrandByCategoryId(Long categoryId, Pageable pageable);
    List<SimpleBrandDto> findBySearchConditions(final String keyword, final Pageable pageable);
}
