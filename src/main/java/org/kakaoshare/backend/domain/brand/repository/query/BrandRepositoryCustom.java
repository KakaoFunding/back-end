package org.kakaoshare.backend.domain.brand.repository.query;

import org.kakaoshare.backend.domain.brand.entity.Brand;

import java.util.List;

public interface BrandRepositoryCustom {
    List<Brand> findAllByCategoryId(Long categoryId);
}
