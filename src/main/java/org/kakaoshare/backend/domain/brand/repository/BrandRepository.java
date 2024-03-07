package org.kakaoshare.backend.domain.brand.repository;

import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.brand.repository.query.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BrandRepository extends JpaRepository<Brand, Long> , BrandRepositoryCustom {
    List<Brand> findByCategory_CategoryId(Long categoryId);
}
