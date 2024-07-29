package org.kakaoshare.backend.domain.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.dto.CategoryHeaderResponse;
import org.kakaoshare.backend.domain.category.dto.SimpleCategoryDto;
import org.kakaoshare.backend.domain.category.error.CategoryErrorCode;
import org.kakaoshare.backend.domain.category.error.exception.CategoryException;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable(value = "parentCategories", key = "'all'", cacheManager = "cacheManager")
    public List<CategoryDto> getParentCategories() {
        return categoryRepository.findAllParentCategories();
    }

    @Cacheable(value = "parentCategory", key = "#categoryId", cacheManager = "cacheManager")
    public CategoryDto getParentCategory(final Long categoryId) {
        return categoryRepository.findParentCategoryWithChildren(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    @Cacheable(value = "childCategory", key = "{#categoryId, #subcategoryId}", cacheManager = "cacheManager")
    public CategoryDto getChildCategory(final Long categoryId, final Long subcategoryId) {
        return categoryRepository.findChildCategoryWithParentCheck(categoryId, subcategoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.INVALID_SUB_CATEGORY_ID));
    }

    @Cacheable(value = "categoryHeaderResponse", key = "#categoryId", cacheManager = "cacheManager")
    public CategoryHeaderResponse getHeaderResponse(final Long categoryId) {
        Long brandCount = categoryRepository.countBrand(categoryId);
        Long productCount = categoryRepository.countProduct(categoryId);
        List<SimpleCategoryDto> list=categoryRepository.findChildrenCategory(categoryId);
        return CategoryHeaderResponse.of(brandCount, productCount, list);
    }
}
