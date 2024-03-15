package org.kakaoshare.backend.domain.product.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;

@Getter
@Builder
public class DetailResponse {
    private final Long productId;
    private final String name;
    private final BigDecimal price;
    private final String type;
    private final String productName;
    private final Boolean hasPhoto;
    private final List<Option> options;
    private final Brand brand;
    private final String origin;
    private final String manufacturer;
    private final String tel;
    private final String deliverDescription;
    private final String billingNotice;
    private final String caution;
    private final List<ProductThumbnail> productThumbnails;
    public static DetailResponse from(final Product product) {
        return DetailResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .hasPhoto(product.getProductDetail().getHasPhoto())
                .productName(product.getProductDetail().getProductName())
                .origin(product.getProductDetail().getOrigin())
                .manufacturer(product.getProductDetail().getManufacturer())
                .tel(product.getProductDetail().getTel())
                .deliverDescription(product.getProductDetail().getDeliverDescription())
                .billingNotice(product.getProductDetail().getBillingNotice())
                .caution(product.getProductDetail().getCaution())
                .productThumbnails(product.getProductThumbnails())
                .options(product.getOptions())
                .brand(product.getBrand())
                .build();
    }
}
