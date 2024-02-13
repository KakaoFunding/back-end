package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    public static final int MAX_LEVEL = 2;
    public static final String ROOT_CATEGORY_NAME = "Root";
    private final CategoryRepository categoryRepository;
    
    //TODO 2024 02 13 14:02:23 : 모든 카테고리 목록 조회
    
    public CategoryDto createCategoryRoot() {
        Map<Long, List<CategoryDto>> groupingByParentId = categoryRepository.findAllWithChildren()
                .stream()
                .map(CategoryDto::of)
                .collect(groupingBy(CategoryDto::getCategoryId));
        
        CategoryDto rootCategoryDto = new CategoryDto(0L, ROOT_CATEGORY_NAME, 0, null);
        addSubCategories(rootCategoryDto, groupingByParentId,0);
        
        return rootCategoryDto;
    }
    
    private void addSubCategories(CategoryDto parent, Map<Long, List<CategoryDto>> categoryGroup,Integer level) {
        if(level>= MAX_LEVEL)
            return;
        List<CategoryDto> subCategories = categoryGroup.get(parent.getCategoryId());
        
        if (subCategories == null)
            return;
        parent.setSubCategories(subCategories);
        
        subCategories.forEach(s -> addSubCategories(s, categoryGroup,level+1));
    }
}
