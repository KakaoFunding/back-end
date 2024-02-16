package org.kakaoshare.backend.domain.category.repository.query;

import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findAllWithChildren();
}