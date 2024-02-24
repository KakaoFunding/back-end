package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryDto getRootCategory() {
        Category rootCategory = categoryRepository.findRoot().orElseThrow();
        return CategoryDto.from(rootCategory);
    }
    
    public CategoryDto getSpecificCategory(Long categoryId){
        Category category = categoryRepository.findByCategoryIdWithParentAndChildren(categoryId).orElseThrow();
        return CategoryDto.from(category);
    }
}
