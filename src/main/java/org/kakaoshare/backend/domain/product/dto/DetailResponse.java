package org.kakaoshare.backend.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.math.BigDecimal;
import java.util.List;

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
                .options(null)//TODO 2024 03 21 16:34:14 : 추후 수정 필요
                .brand(product.getBrand())
                .build();
    }
}
