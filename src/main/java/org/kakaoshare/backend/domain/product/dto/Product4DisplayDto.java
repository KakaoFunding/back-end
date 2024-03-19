package org.kakaoshare.backend.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class Product4DisplayDto extends ProductDto {
    private final String brandName;
    private final boolean isWished;
    private final Long wishCount;

    @QueryProjection
    public Product4DisplayDto(final Long productId, final String name,
                              final String photo, final Long price,
                              final String brandName, final Long wishCount) {
        super(productId, name, photo, price);
        this.brandName = brandName;
        this.wishCount = wishCount;
        this.isWished = false;
    }
    
    @Override
    public String toString() {
        return "Product4DisplayDto{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", price=" + price +
                ", brandName='" + brandName + '\'' +
                ", wishCount=" + wishCount + '\'' +
                ", isWished=" + isWished +
                '}';
    }
}
