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
public class DetailResponse {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String type;
    private String description;
    private List<ProductDescriptionPhoto> descriptionPhotos;
    private Boolean hasPhoto;
    private String productName;
    private String origin;
    private String manufacturer;
    private String tel;
    private String deliverDescription;
    private String billingNotice;
    private String caution;
    private List<Option> options;
    private Brand brand;

    public static DetailResponse from(final Product product) {
        return DetailResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .description(product.getProductDetail().getDescription())
                .descriptionPhotos(product.getProductDescriptionPhotos())
                .hasPhoto(product.getProductDetail().getHasPhoto())
                .productName(product.getProductDetail().getProductName())
                .origin(product.getProductDetail().getOrigin())
                .manufacturer(product.getProductDetail().getManufacturer())
                .tel(product.getProductDetail().getTel())
                .deliverDescription(product.getProductDetail().getDeliverDescription())
                .billingNotice(product.getProductDetail().getBillingNotice())
                .caution(product.getProductDetail().getCaution())
                .options(product.getOptions())
                .brand(product.getBrand())
                .build();
    }
}
