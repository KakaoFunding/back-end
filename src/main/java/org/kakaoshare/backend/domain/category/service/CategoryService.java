package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.error.CategoryErrorCode;
import org.kakaoshare.backend.domain.category.error.exception.CategoryNotFoundException;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    public List<CategoryDto> getParentCategories() {
        List<Category> categories = categoryRepository.findAllParentCategories();
        return categories.stream().map(CategoryDto::from).toList();
    }
    
    public CategoryDto getParentCategory(final Long categoryId) {
        Category category = categoryRepository.findParentCategoryWithChildren(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return CategoryDto.from(category);
    }
    
    public CategoryDto getChildCategory(final Long categoryId, final Long subcategoryId) {
        Category category = categoryRepository.findChildCategoryWithParentCheck(categoryId, subcategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CategoryErrorCode.INVALID_SUB_CATEGORY_ID));
        return CategoryDto.from(category);
    }
}
