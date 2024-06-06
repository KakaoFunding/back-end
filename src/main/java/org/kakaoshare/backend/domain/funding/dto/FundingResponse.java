package org.kakaoshare.backend.domain.funding.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class FundingResponse {
    private final Long fundingId;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDate expiredAt;
    private final Long goalAmount;
    private final String brandName;
    private final String productName;
    private final String productImage;
    private final Long accumulateAmount;
    private final Long productId;

    public static FundingResponse from(Funding funding) {
        Product product = funding.getProduct();

        return FundingResponse.builder()
                .fundingId(funding.getFundingId())
                .status(funding.getStatus().name())
                .createdAt(funding.getCreatedAt())
                .expiredAt(funding.getExpiredAt())
                .goalAmount(funding.getGoalAmount())
                .brandName(product.getBrand().getName())
                .productName(product.getName())
                .productImage(product.getPhoto())
                .accumulateAmount(funding.getAccumulateAmount())
                .productId(product.getProductId())
                .build();
    }
}
