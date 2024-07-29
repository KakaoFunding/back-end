package org.kakaoshare.backend.domain.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleCategoryDto {
    private Long categoryId;
    private String categoryName;

    @Builder
    @QueryProjection
    public SimpleCategoryDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static SimpleCategoryDto from(final CategoryDto categoryDto) {
        return SimpleCategoryDto.builder()
                .categoryId(categoryDto.getCategoryId())
                .categoryName(categoryDto.getCategoryName())
                .build();
    }
}
