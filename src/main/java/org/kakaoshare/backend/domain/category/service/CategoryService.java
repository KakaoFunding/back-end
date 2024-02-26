package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<CategoryDto> getParentCategory() {
        List<Category> categories = categoryRepository.findAllParentCategories();
        return categories.stream().map(CategoryDto::from).toList();
    }
    
    public CategoryDto getSpecificCategory(Long categoryId){
        Category category = categoryRepository.findByCategoryIdWithParentAndChildren(categoryId).orElseThrow();
        return CategoryDto.from(category);
    }
}
