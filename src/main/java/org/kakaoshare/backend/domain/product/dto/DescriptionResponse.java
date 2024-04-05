package org.kakaoshare.backend.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;

import java.util.List;

@Getter
@Builder
public class DescriptionResponse {
    private final Long productId;
    private final String name;
    private final Long price;
    private final String type;
    private final String description;
    private final List<String> descriptionPhotos;
    private final String productName;
    private final List<OptionResponse> options;
    private final List<String> productThumbnails;
    private final String brandName;

    public static DescriptionResponse from(final Product product, List<String> descriptionPhotosUrls,
                                           List<OptionResponse> optionsResponses, List<String> productThumbnailsUrls) {
        return DescriptionResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .description(product.getProductDetail().getDescription())
                .descriptionPhotos(descriptionPhotosUrls)
                .productName(product.getProductDetail().getProductName())
                .options(optionsResponses)
                .productThumbnails(productThumbnailsUrls)
                .brandName(product.getBrand().getName())
                .build();
    }
}
