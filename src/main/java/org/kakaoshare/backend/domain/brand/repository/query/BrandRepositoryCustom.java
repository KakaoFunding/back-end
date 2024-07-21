package org.kakaoshare.backend.domain.brand.repository.query;

import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {
    List<SimpleBrandDto> findAllSimpleBrandByCategoryId(Long categoryId);
    List<SimpleBrandDto> findBySearchConditions(final String keyword, final Pageable pageable);
}
