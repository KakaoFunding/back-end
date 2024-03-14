package org.kakaoshare.backend.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductDto {
    protected final Long productId;
    protected final String name;
    protected final String photo;
    protected final BigDecimal price;
    
    @QueryProjection
    public ProductDto(final Long productId, final String name,
                       final String photo, final BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.photo = photo;
        this.price = price;
    }
}
