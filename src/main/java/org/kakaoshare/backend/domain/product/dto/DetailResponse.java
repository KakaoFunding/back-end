package org.kakaoshare.backend.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;
import java.util.List;

@Getter
@Builder
public class DetailResponse {
    private final Long productId;
    private final String name;
    private final Long price;
    private final String type;
    private final String productName;
    private final List<OptionResponse> options;
    private final String brandName;
    private final Long brandId;
    private final String brandThumbnail;
    private final String origin;
    private final String manufacturer;
    private final String tel;
    private final String deliverDescription;
    private final String billingNotice;
    private final String caution;
    private final List<String> productThumbnails;
    private final int wishCount;
    private final boolean isWish;

    public static DetailResponse of(final Product product, List<OptionResponse> optionsResponses, Boolean isWished) {
        String origin = product.getProductDetail() != null ? product.getProductDetail().getOrigin() : null;
        String manufacturer =
                product.getProductDetail() != null ? product.getProductDetail().getManufacturer() : null;
        String tel = product.getProductDetail() != null ? product.getProductDetail().getTel() : null;
        String deliverDescription =
                product.getProductDetail() != null ? product.getProductDetail().getDeliverDescription() : null;
        String billingNotice =
                product.getProductDetail() != null ? product.getProductDetail().getBillingNotice() : null;
        String caution = product.getProductDetail() != null ? product.getProductDetail().getCaution() : null;

        return DetailResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .productName(product.getName())
                .origin(origin)
                .manufacturer(manufacturer)
                .tel(tel)
                .deliverDescription(deliverDescription)
                .billingNotice(billingNotice)
                .caution(caution)
                .productThumbnails(product.getThumbnailUrls())
                .options(optionsResponses)
                .brandName(product.getBrandName())
                .brandId(product.getBrand().getBrandId())
                .brandThumbnail(product.getBrand().getIconPhoto())
                .wishCount(product.getWishCount())
                .isWish(isWished)
                .build();
    }
}
