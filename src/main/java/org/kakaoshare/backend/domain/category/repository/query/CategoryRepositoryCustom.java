package org.kakaoshare.backend.domain.category.repository.query;

import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryCustom {
    List<Category> findAllWithChildren();
    
    Optional<Category> findByCategoryIdWithChildren(Long categoryId);
}