package org.kakaoshare.backend.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;

import java.util.List;
import org.kakaoshare.backend.domain.wish.entity.Wish;

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
    private final Long brandId;
    private final String brandThumbnail;
    private final int wishCount;
    private final boolean isWish;

    public static DescriptionResponse of(final Product product, List<String> descriptionPhotosUrls,
                                         List<OptionResponse> optionsResponses, List<String> productThumbnailsUrls, Boolean isWished) {

        List<String> thumbnails;
        if (product.getProductThumbnails().isEmpty() && product.getPhoto() != null) {
            thumbnails = List.of(product.getPhoto());
        } else {
            thumbnails = product.getProductThumbnails().stream()
                    .map(ProductThumbnail::getThumbnailUrl)
                    .toList();
        }

        return DescriptionResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .description(product.getProductDetail() != null ? product.getProductDetail().getDescription() : "기본 설명") //todo 추후 변경할로직
                .descriptionPhotos(descriptionPhotosUrls)
                .productName(product.getName())
                .options(optionsResponses)
                .productThumbnails(thumbnails)
                .brandName(product.getBrand().getName())
                .brandId(product.getBrand().getBrandId())
                .brandThumbnail(product.getBrand().getIconPhoto())
                .wishCount(product.getWishCount())
                .isWish(isWished)
                .build();
    }
}
