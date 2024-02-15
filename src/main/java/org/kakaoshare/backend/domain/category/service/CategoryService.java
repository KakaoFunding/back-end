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
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private static final CategoryDto ROOT_CATEGORY_DTO = new CategoryDto(-1L, "Root", null);
    private final CategoryRepository categoryRepository;
    
    
    public CategoryDto createCategoryRoot() {
        List<Category> categories = categoryRepository.findAllWithChildren();
        
        Map<Long, List<CategoryDto>> parentGroup
                = groupCategories(categories, category -> !hasParent(category), this::mapToParentCategoryDto);
        Map<Long, List<CategoryDto>> childGroup
                = groupCategories(categories, category -> hasParent(category), CategoryDto::from);
        
        childGroup.forEach((parentId, subCategories) ->
                parentGroup.merge(parentId, subCategories, (existingSubCategories, newSubCategories) -> {
                    existingSubCategories.addAll(newSubCategories);
                    return existingSubCategories;
                }));
        
        addSubCategories(ROOT_CATEGORY_DTO, parentGroup);
        
        return ROOT_CATEGORY_DTO;
    }
    
    private static boolean hasParent(Category category) {
        return Objects.nonNull(category.getParent());
    }
    
    private CategoryDto mapToParentCategoryDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName(), ROOT_CATEGORY_DTO.getCategoryId());
    }
    
    private Map<Long, List<CategoryDto>> groupCategories(List<Category> categories,
                                                         Predicate<Category> filter,
                                                         Function<Category, CategoryDto> mapper) {
        return categories.stream()
                .filter(filter)
                .map(mapper)
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
