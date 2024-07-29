package org.kakaoshare.backend.domain.category.repository.query;

import java.util.List;
import java.util.Optional;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.dto.SimpleCategoryDto;

public interface CategoryRepositoryCustom {

    Optional<CategoryDto> findParentCategoryWithChildren(Long categoryId);
    
    Optional<CategoryDto> findChildCategoryWithParentCheck(final Long categoryId, final Long subcategoryId);
    
    
    List<CategoryDto> findAllParentCategories();
    Long countBrand(Long categoryId);
    Long countProduct(Long categoryId);

    List<SimpleCategoryDto> findChildrenCategory(Long categoryId);
}