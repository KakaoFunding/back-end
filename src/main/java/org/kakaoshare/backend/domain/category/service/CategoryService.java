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
    private static final CategoryDto ROOT_CATEGORY_DTO = new CategoryDto(-1L, "Root", null);
    private final CategoryRepository categoryRepository;
    
    
    public CategoryDto createCategoryRoot() {
        List<Category> categories = categoryRepository.findAllWithChildren();
        
        Map<Long, List<CategoryDto>> parentGroup = groupParentCategories(categories);
        
        Map<Long, List<CategoryDto>> childGroup = groupChildCategories(categories);
        
        childGroup.forEach((parentId, subCategories) ->
                parentGroup.merge(parentId, subCategories, (existingSubCategories, newSubCategories) -> {
                    existingSubCategories.addAll(newSubCategories);
                    return existingSubCategories;
                }));
        
        addSubCategories(ROOT_CATEGORY_DTO, parentGroup);
        
        return ROOT_CATEGORY_DTO;
    }
    

    private static Map<Long, List<CategoryDto>> groupParentCategories(final List<Category> categories) {
        return categories
                .stream()
                .filter(category -> Objects.isNull(category.getParent()))
                .map(category -> new CategoryDto(
                        category.getCategoryId(),
                        category.getName(),
                        CategoryService.ROOT_CATEGORY_DTO.getCategoryId()))//부모 ID를 Root로 임의 지정(DB에는 존재하지 않기 때문)
                .collect(groupingBy(CategoryDto::getParentId));
    }
    

    private static Map<Long, List<CategoryDto>> groupChildCategories(final List<Category> categories) {
        return categories
                .stream()
                .filter(category -> Objects.nonNull(category.getParent()))
                .map(CategoryDto::from)
                .collect(groupingBy(CategoryDto::getParentId));
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
