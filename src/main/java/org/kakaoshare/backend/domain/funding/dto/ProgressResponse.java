package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.funding.entity.Funding;

import java.util.Optional;
import org.kakaoshare.backend.domain.product.entity.Product;

@Getter
@Builder
public class ProgressResponse {

    private static final Long ZERO = 0L;
    private static final int SCALE = 4;
    private static final double DEFAULT_PROGRESS_RATE = 0.0;
    private static final double PERCENT_MULTIPLIER = 100.0;

    private final Long fundingId;
    private final Double progressRate;
    private final Long remainAmount;
    private final Long goalAmount;
    private final Long accumulateAmount;
    private final Long productId;
    private final Long brandId;
    private final String brandPhoto;
    private final String productPhoto;
    private final String brandName;
    private final String productName;


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
