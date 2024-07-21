package org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;

@Getter
@Builder
public class GiftDescriptionResponse {
    private final Long productId;
    private final String name;
    private final Long price;
    private final String type;
    private final String productName;
    private final String brandName;
    private final String origin;
    private final String manufacturer;
    private final String tel;
    private final String deliverDescription;
    private final String billingNotice;
    private final String caution;
    private final String giftThumbnail;
    public static GiftDescriptionResponse of(final Product product, final ProductThumbnail productThumbnail) {
        return GiftDescriptionResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .productName(product.getProductDetail().getProductName())
                .origin(product.getProductDetail().getOrigin())
                .manufacturer(product.getProductDetail().getManufacturer())
                .tel(product.getProductDetail().getTel())
                .deliverDescription(product.getProductDetail().getDeliverDescription())
                .billingNotice(product.getProductDetail().getBillingNotice())
                .caution(product.getProductDetail().getCaution())
                .giftThumbnail(productThumbnail.getThumbnailUrl())
                .brandName(product.getBrandName())
                .build();
    }
}
