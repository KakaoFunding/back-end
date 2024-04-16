package org.kakaoshare.backend.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.option.entity.Option;
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
    private final String origin;
    private final String manufacturer;
    private final String tel;
    private final String deliverDescription;
    private final String billingNotice;
    private final String caution;
    private final List<ProductThumbnail> productThumbnails;
    public static DetailResponse from(final Product product,List<OptionResponse> optionsResponses) {
        String origin = product.getProductDetail() != null ? product.getProductDetail().getOrigin() : "정보 없음";
        String manufacturer = product.getProductDetail() != null ? product.getProductDetail().getManufacturer() : "정보 없음";
        String tel = product.getProductDetail() != null ? product.getProductDetail().getTel() : "정보 없음";
        String deliverDescription = product.getProductDetail() != null ? product.getProductDetail().getDeliverDescription() : "정보 없음";
        String billingNotice = product.getProductDetail() != null ? product.getProductDetail().getBillingNotice() : "정보 없음";
        String caution = product.getProductDetail() != null ? product.getProductDetail().getCaution() : "정보 없음";

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
                .productThumbnails(product.getProductThumbnails())
                .options(optionsResponses)
                .brandName(product.getBrandName())
                .build();
    }
}
