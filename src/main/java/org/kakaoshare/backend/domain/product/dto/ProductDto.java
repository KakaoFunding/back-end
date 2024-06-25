package org.kakaoshare.backend.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.kakaoshare.backend.domain.product.entity.Product;

@Getter
public class ProductDto {
    protected final Long productId;
    protected final String name;
    protected final String photo;
    protected final Long price;
    protected final String brandName;

    @QueryProjection
    public ProductDto(final Long productId, final String name,
                      final String photo, final Long price,
                      final String brandName) {
        this.productId = productId;
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.brandName = brandName;
    }

    public static ProductDto from(final Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getPhoto(),
                product.getPrice(),
                product.getBrandName()
        );
    }
}
