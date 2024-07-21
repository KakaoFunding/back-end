package org.kakaoshare.backend.domain.product.dto;

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDetail;
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
        ProductDetail detail = product.getProductDetail();

        String origin = Optional.ofNullable(detail).map(ProductDetail::getOrigin).orElse(null);
        String manufacturer = Optional.ofNullable(detail).map(ProductDetail::getManufacturer).orElse(null);
        String tel = Optional.ofNullable(detail).map(ProductDetail::getTel).orElse(null);
        String deliverDescription = Optional.ofNullable(detail).map(ProductDetail::getDeliverDescription).orElse(null);
        String billingNotice = Optional.ofNullable(detail).map(ProductDetail::getBillingNotice).orElse(null);
        String caution = Optional.ofNullable(detail).map(ProductDetail::getCaution).orElse(null);

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
