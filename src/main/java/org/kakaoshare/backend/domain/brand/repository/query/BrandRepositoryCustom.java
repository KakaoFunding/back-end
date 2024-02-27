package org.kakaoshare.backend.domain.brand.repository.query;

import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;

import java.util.List;

public interface BrandRepositoryCustom {
    List<SimpleBrandDto> findAllSimpleBrandByChildId(Long categoryId);
    
    List<SimpleBrandDto> findAllSimpleBrandByParentId(Long categoryId);
}
