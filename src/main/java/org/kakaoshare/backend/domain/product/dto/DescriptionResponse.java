package org.kakaoshare.backend.domain.product.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;

@Getter
@Builder
public class DescriptionResponse {
    private final Long productId;
    private final String name;
    private final BigDecimal price;
    private final String type;
    private final String description;
    private final List<ProductDescriptionPhoto> descriptionPhotos;
    private final String productName;
    private final List<Option> options;
    private final List<ProductThumbnail> productThumbnails;
    private final String brandName;

    public static DescriptionResponse from(final Product product) {
        return DescriptionResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .description(product.getProductDetail().getDescription())
                .descriptionPhotos(product.getProductDescriptionPhotos())
                .productName(product.getProductDetail().getProductName())
                .productThumbnails(product.getProductThumbnails())
                .options(product.getOptions())
                .brandName(product.getBrand().getName())
                .build();
    }
}
