package org.kakaoshare.backend.domain.category.repository.query;

import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryCustom {
    
    Optional<Category> findParentCategoryWithChildren(Long categoryId);
    
    Optional<Category> findChildCategoryWithParentCheck(final Long categoryId, final Long subcategoryId);
    
    
    List<Category> findAllParentCategories();
    Long countBrand(Long categoryId);
    Long countProduct(Long categoryId);
    
}