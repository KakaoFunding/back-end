package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;

@Getter
@Builder
public class FundingResponse {
    private final Long fundingId;
    private final String status;
    private final LocalDate expiredAt;
    private final Long goalAmount;
    private final String brandName;
    private final String productName;
    private final String productImage;

    public static FundingResponse from(Funding funding) {
        Product product = funding.getProduct();
        String productImage = null;
        if (product.getProductThumbnails() != null && !product.getProductThumbnails().isEmpty()) {
            productImage = product.getProductThumbnails().get(0).getThumbnailUrl();
        }

        return FundingResponse.builder()
                .fundingId(funding.getFundingId())
                .status(funding.getStatus().name())
                .expiredAt(funding.getExpiredAt())
                .goalAmount(funding.getGoalAmount())
                .brandName(product.getBrand().getName())
                .productName(product.getName())
                .productImage(productImage)
                .build();
    }
}
