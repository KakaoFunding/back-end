package org.kakaoshare.backend.domain.category.repository.query;

import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.Optional;

public interface CategoryRepositoryCustom {
    
    Optional<Category> findByCategoryIdWithParentAndChildren(Long categoryId);
    
    Optional<Category> findRoot();
    
}