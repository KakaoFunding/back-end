package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
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
    private final double progressRate;
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
        Long goalAmount = funding.getGoalAmount();
        Long accumulateAmount = funding.getAccumulateAmount();
        double progressRate = Optional.of(goalAmount)
                .filter(goalAmountValue -> goalAmountValue.compareTo(ZERO) != 0)
                .map(goalAmountValue -> divide(accumulateAmount, goalAmountValue, SCALE) * PERCENT_MULTIPLIER)
                .orElse(DEFAULT_PROGRESS_RATE);
        Long remainAmount = goalAmount - accumulateAmount;

        Product product = funding.getProduct();
        Brand brand = product.getBrand();

        return ProgressResponse.builder()
                .fundingId(funding.getFundingId())
                .progressRate(progressRate)
                .remainAmount(remainAmount)
                .goalAmount(goalAmount)
                .accumulateAmount(accumulateAmount)
                .productId(product.getProductId())
                .brandId(brand.getBrandId())
                .brandPhoto(brand.getIconPhoto())
                .productPhoto(product.getPhoto())
                .brandName(brand.getName())
                .productName(product.getName())
                .build();
    }
    
    private static Double divide(long numerator, long denominator, int scale) {
        long scaledNumerator = numerator * (long) Math.pow(10, scale + 1);
        long rawResult = scaledNumerator / denominator;
        long remainder = rawResult % 10;
        rawResult /= 10;
        if (remainder >= 5) {
            rawResult += 1;
        }
        return rawResult / Math.pow(10, scale);
    }
}
