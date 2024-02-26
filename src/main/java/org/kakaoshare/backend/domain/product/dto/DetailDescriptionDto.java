package org.kakaoshare.backend.domain.product.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDescriptionPhoto;

@Getter
@Builder
public class DetailDescriptionDto {
    private final Long productId;
    private final String name;
    private final BigDecimal price;
    private final String type;
    private final String description;
    private final List<ProductDescriptionPhoto> descriptionPhotos;
    private final Boolean hasPhoto;
    private final String productName;
    private final List<Option> options;
    private final Brand brand;

    public static DetailDescriptionDto from(final Product product) {
        return DetailDescriptionDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .description(product.getProductDetail().getDescription())
                .descriptionPhotos(product.getProductDescriptionPhotos())
                .hasPhoto(product.getProductDetail().getHasPhoto())
                .productName(product.getProductDetail().getProductName())
                .options(product.getOptions())
                .brand(product.getBrand())
                .build();
    }
}
