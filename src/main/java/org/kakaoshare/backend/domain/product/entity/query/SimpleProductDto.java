package org.kakaoshare.backend.domain.product.entity.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SimpleProductDto {
    private String name;
    private String photo;
    private BigDecimal price;
    private String brandName;
    private Long wishCount;
    @QueryProjection
    public SimpleProductDto(final String name, final String photo, final BigDecimal price,
                             final String brandName, final Long wishCount) {
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.brandName = brandName;
        this.wishCount = wishCount;
    }
    //    @QueryProjection
//    public SimpleProductDto(Product product) {
//        this.name = product.getName();
//        this.photo = product.getPhoto();
//        this.price = product.getPrice();
//        this.brandName = product.getBrand().getName();
//        this.wishCount = (long) product.getWishes().size();
//    }
}
