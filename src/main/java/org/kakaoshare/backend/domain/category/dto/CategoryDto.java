package org.kakaoshare.backend.domain.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@JsonSerialize
public class CategoryDto {
    public static final long PARENT_ID = -1L;
    private final Long categoryId;
    private final String categoryName;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long parentId;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<CategoryDto> subCategories = new ArrayList<>();
    private final TabType defaultTab;
    private int level;
    
    
    @Builder
    public CategoryDto(Long categoryId, String categoryName, Long parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.defaultTab = TabType.BRAND;//브랜드를 조회하는것이 화면 로딩과정에서 쿼리를 최소화 가능해보임
    }

    public static CategoryDto from(final Category category) {
        CategoryDto categoryDto = CategoryDto
                .getCategoryDtoBuilder(category)
                .build();

        giveLevelAndSubCategories(categoryDto, category);

        return categoryDto;
    }

    private static void giveLevelAndSubCategories(CategoryDto dto, Category category) {
        dto.level=2;
        if (!category.isChildEmpty()) {
            dto.level = 1;
            dto.getSubCategories().addAll(getChildrenDtos(category));
        }
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

    private static CategoryDtoBuilder getCategoryDtoBuilder(final Category category) {
        Long parentId = Optional
                .ofNullable(category.getParent())
                .map(Category::getCategoryId)
                .orElse(null);

        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .parentId(parentId);
    }
}
