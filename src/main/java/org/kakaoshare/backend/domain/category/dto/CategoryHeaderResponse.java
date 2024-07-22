package org.kakaoshare.backend.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryHeaderResponse {
    private Long brandCount;
    private Long productCount;
    private Long totalCount;
    private List<SimpleCategoryDto> simpleCategoryDtos;
    @Builder
    private CategoryHeaderResponse(final Long brandCount, final Long productCount, Long totalCount, final List<SimpleCategoryDto> simpleCategoryDtos) {
        this.brandCount = brandCount;
        this.productCount = productCount;
        this.totalCount = totalCount;
        this.simpleCategoryDtos = simpleCategoryDtos;
    }
    
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
