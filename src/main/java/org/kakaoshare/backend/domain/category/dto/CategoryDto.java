package org.kakaoshare.backend.domain.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonSerialize
@NoArgsConstructor
public class CategoryDto {
    public static final long PARENT_ID = -1L;
    private Long categoryId;
    private String categoryName;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentId;
    
    @JsonInclude(Include.NON_EMPTY)
    private final List<CategoryDto> subCategories = new ArrayList<>();

    private final TabType defaultTab=TabType.BRAND;

    @QueryProjection
    public CategoryDto(Long categoryId, String categoryName, Long parentId, List<CategoryDto> subCategories) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.subCategories.addAll(subCategories);
    }

    @QueryProjection
    public CategoryDto(Long categoryId, String categoryName, Long parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
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
