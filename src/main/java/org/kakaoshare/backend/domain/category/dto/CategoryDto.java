package org.kakaoshare.backend.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.brand.dto.TabType;
import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class CategoryDto {
    public static final long ROOT_ID = -1L;
    private final Long categoryId;
    private final String categoryName;
    private final Long parentId;
    private final List<CategoryDto> subCategories = new ArrayList<>();
    private final TabType defaultTab;
    
    @Builder
    public CategoryDto(Long categoryId, String categoryName, Long parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.defaultTab = TabType.BRAND;//브랜드를 조회하는것이 화면 로딩과정에서 쿼리를 최소화 가능해보임
    }
    
    public static CategoryDto from(final Category category) {
        if (Objects.isNull(category.getParent())) {
            CategoryDto rootDto = CategoryDto.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getName())
                    .parentId(ROOT_ID)
                    .build();
            rootDto.getSubCategories().addAll(getChildrenDtos(category));
            return rootDto;
        }
        
        CategoryDto nonRootDto = CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .parentId(category.getParent().getCategoryId())
                .build();
        
        if (!category.getChildren().isEmpty()) {
            List<CategoryDto> childrenDtos = getChildrenDtos(category);
            nonRootDto.getSubCategories().addAll(childrenDtos);
        }
        
        return nonRootDto;
    }
    
    private static List<CategoryDto> getChildrenDtos(final Category category) {
        return category.getChildren().stream()
                .map(CategoryDto::from)
                .toList();
    }
    
    
    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", subCategory id=" +
                subCategories.stream()
                        .map(CategoryDto::getCategoryId)
                        .toList() +
                ", defaultTab=" + defaultTab +
                '}';
    }
}
