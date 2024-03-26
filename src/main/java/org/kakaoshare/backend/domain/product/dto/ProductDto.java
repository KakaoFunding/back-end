package org.kakaoshare.backend.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductDto {
    protected final Long productId;
    protected final String name;
    protected final String photo;
    protected final Long price;
    
    @QueryProjection
    public ProductDto(final Long productId, final String name,
                       final String photo, final Long price) {
        this.productId = productId;
        this.name = name;
        this.photo = photo;
        this.price = price;
    }
}
