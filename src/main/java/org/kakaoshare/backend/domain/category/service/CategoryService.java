package org.kakaoshare.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private static final String ROOT_CATEGORY_NAME = "Root";
    private final CategoryRepository categoryRepository;
    
    
    public CategoryDto createCategoryRoot() {
        List<Category> categories = categoryRepository.findAllWithChildren();
        CategoryDto rootCategoryDto = new CategoryDto(0L, ROOT_CATEGORY_NAME, 0L);
        
        Map<Long, List<CategoryDto>> parentGroup = categories
                .stream()
                .filter(category -> Objects.isNull(category.getParent()))
                .map(category -> new CategoryDto(
                        category.getCategoryId(),
                        category.getName(),
                        rootCategoryDto.getCategoryId()))//부모 ID를 Root로 임의 지정(DB에는 존재하지 않기 때문)
                .collect(groupingBy(CategoryDto::getParentId));
        
        Map<Long, List<CategoryDto>> childGroup = categories
                .stream()
                .filter(category -> Objects.nonNull(category.getParent()))
                .map(CategoryDto::of)
                .collect(groupingBy(CategoryDto::getParentId));
        
        childGroup.forEach((parentId, subCategories) ->
                parentGroup.merge(parentId, subCategories, (existingSubCategories, newSubCategories) -> {
                    existingSubCategories.addAll(newSubCategories);
                    return existingSubCategories;
                }));
        
        addSubCategories(rootCategoryDto, parentGroup);
        
        return rootCategoryDto;
    }
    
    private void addSubCategories(CategoryDto parent, Map<Long, List<CategoryDto>> categoryGroup) {
        List<CategoryDto> subCategories = categoryGroup.get(parent.getCategoryId());
        
        if (subCategories == null) {
            return;
        }
        
        parent.getSubCategories().addAll(subCategories);
        
        subCategories.forEach(s -> addSubCategories(s, categoryGroup));
    }
}
