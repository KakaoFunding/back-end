package org.kakaoshare.backend.domain.funding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.funding.entity.Funding;

import java.util.Optional;
import org.kakaoshare.backend.domain.product.entity.Product;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponse {

    private static final Long ZERO = 0L;
    private static final int SCALE = 4;
    private static final double DEFAULT_PROGRESS_RATE = 0.0;
    private static final double PERCENT_MULTIPLIER = 100.0;
    @JsonInclude(Include.NON_NULL)
    private Long fundingId;
    @JsonInclude(Include.NON_NULL)
    private Double progressRate;
    @JsonInclude(Include.NON_NULL)
    private Long remainAmount;
    @JsonInclude(Include.NON_NULL)
    private Long goalAmount;
    @JsonInclude(Include.NON_NULL)
    private Long accumulateAmount;
    @JsonInclude(Include.NON_NULL)
    private Long productId;
    @JsonInclude(Include.NON_NULL)
    private Long brandId;
    @JsonInclude(Include.NON_NULL)
    private String brandPhoto;
    @JsonInclude(Include.NON_NULL)
    private String productPhoto;
    @JsonInclude(Include.NON_NULL)
    private String brandName;
    @JsonInclude(Include.NON_NULL)
    private String productName;


    public static ProgressResponse from(Funding funding) {
        Product product = funding.getProduct();
        Brand brand = product.getBrand();

        return ProgressResponse.builder()
                .fundingId(funding.getFundingId())
                .progressRate(funding.calculateProgressRate())
                .remainAmount(funding.calculateRemainAmount())
                .goalAmount(funding.getGoalAmount())
                .accumulateAmount(funding.getAccumulateAmount())
                .productId(product.getProductId())
                .brandId(brand.getBrandId())
                .brandPhoto(brand.getIconPhoto())
                .productPhoto(product.getPhoto())
                .brandName(brand.getName())
                .productName(product.getName())
                .build();
    }

    public static ProgressResponse empty() {
        return ProgressResponse.builder()
                .fundingId(null)
                .progressRate(null)
                .remainAmount(null)
                .goalAmount(null)
                .accumulateAmount(null)
                .productId(null)
                .brandId(null)
                .brandPhoto(null)
                .productPhoto(null)
                .brandName(null)
                .productName(null)
                .build();
    }
}
