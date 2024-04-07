package org.kakaoshare.backend.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.category.entity.Category;

@Getter
public class SimpleCategoryDto {
    private final Long categoryId;
    private final String categoryName;
    @Builder
    private SimpleCategoryDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
    
    public static SimpleCategoryDto from(final Category category){
        return SimpleCategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .build();
    }
}
