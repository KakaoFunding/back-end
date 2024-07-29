package org.kakaoshare.backend.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryHeaderResponse {
    private Long brandCount;
    private Long productCount;
    private Long totalCount;
    private List<SimpleCategoryDto> simpleCategoryDtos;

    public static CategoryHeaderResponse of(final Long brandCount, final Long productCount, final List<SimpleCategoryDto> simpleCategoryDtos){
        return CategoryHeaderResponse
                .builder()
                .brandCount(brandCount)
                .productCount(productCount)
                .totalCount(brandCount+productCount)
                .simpleCategoryDtos(simpleCategoryDtos)
                .build();
    }
}
