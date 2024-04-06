package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.dto.CategoryHeaderResponse;
import org.kakaoshare.backend.domain.category.dto.SimpleCategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.error.CategoryErrorCode;
import org.kakaoshare.backend.domain.category.error.exception.CategoryException;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    //TODO 2024 03 31 21:33:04 : 캐싱 구현 후 캐싱된 쿼리 사용
    public List<CategoryDto> getParentCategories() {
        List<Category> categories = categoryRepository.findAllParentCategories();
        return categories.stream()
                .map(CategoryDto::from)
                .toList();
    }
    
    //TODO 2024 03 31 21:32:45 : 캐싱 구현 후 캐싱된 쿼리 사용
    public CategoryDto getParentCategory(final Long categoryId) {
        Category category = categoryRepository.findParentCategoryWithChildren(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return CategoryDto.from(category);
    }
    
    public CategoryDto getChildCategory(final Long categoryId, final Long subcategoryId) {
        Category category = categoryRepository.findChildCategoryWithParentCheck(categoryId, subcategoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.INVALID_SUB_CATEGORY_ID));
        return CategoryDto.from(category);
    }
    
    //TODO 2024 03 31 21:32:16 : 캐싱 구현 후 캐싱된 쿼리 사용
    public CategoryHeaderResponse getHeaderResponse(final Long categoryId) {
        Long brandCount = categoryRepository.countBrand(categoryId);
        Long productCount = categoryRepository.countProduct(categoryId);
        List<SimpleCategoryDto> list = categoryRepository.findParentCategoryWithChildren(categoryId)
                .orElseThrow()
                .getChildren()
                .stream()
                .map(SimpleCategoryDto::from)
                .toList();
        return CategoryHeaderResponse.of(brandCount, productCount, list);
    }
}
