package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리 ID 입니다."));
        return CategoryDto.from(category);
    }
    
    public CategoryDto getChildCategory(final Long categoryId, final Long subcategoryId) {
        Category category = categoryRepository.findChildCategoryWithParentCheck(categoryId, subcategoryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리 ID 입니다.(부모 카테고리 ID 불일치)"));
        return CategoryDto.from(category);
    }
}
