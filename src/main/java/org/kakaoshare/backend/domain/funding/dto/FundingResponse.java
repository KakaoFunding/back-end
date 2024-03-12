package org.kakaoshare.backend.domain.funding.dto;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.product.entity.Product;

@Getter
@Builder
public class FundingResponse {
    private final Long fundingId;
    private final String status;
    private final LocalDate expiredAt;
    private final BigDecimal goalAmount;
    private final String brandName;
    private final String productName;
    private final String productImage;

    public static FundingResponse of(Funding funding, Product product) {
        String productImage = null;
        if (!product.getProductThumbnails().isEmpty()) {
            productImage = product.getProductThumbnails().get(0).getThumbnailUrl();
        }

        return FundingResponse.builder()
                .fundingId(funding.getFundingId())
                .status(funding.getStatus())
                .expiredAt(funding.getExpiredAt())
                .goalAmount(funding.getGoalAmount())
                .brandName(product.getBrand().getName())
                .productName(product.getName())
                .productImage(productImage)
                .build();
    }


}
