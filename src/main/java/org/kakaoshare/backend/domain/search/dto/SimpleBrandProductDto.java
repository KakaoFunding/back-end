package org.kakaoshare.backend.domain.search.dto;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;

import java.util.List;

public record SimpleBrandProductDto(SimpleBrandDto brand, List<Product4DisplayDto> products) {
    @QueryProjection
    public SimpleBrandProductDto(final SimpleBrandDto brand,
                                 final List<Product4DisplayDto> products) {
        this.brand = brand;
        this.products = products;
    }
}
