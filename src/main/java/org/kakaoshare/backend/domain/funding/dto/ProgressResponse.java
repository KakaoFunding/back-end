package org.kakaoshare.backend.domain.funding.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;

@Getter
@Builder
public class ProgressResponse {
    private final Long fundingId;
    private final double progressRate;
    private final BigDecimal remainAmount;
    private final BigDecimal goalAmount;
    private final BigDecimal accumulateAmount;

    public static ProgressResponse from(Funding funding) {
        BigDecimal goalAmount = funding.getGoalAmount();
        BigDecimal accumulateAmount = funding.getAccumulateAmount();
        double progressRate = Optional.of(goalAmount)
                .filter(goalAmountValue -> goalAmountValue.compareTo(BigDecimal.ZERO) != 0)
                .map(goalAmountValue -> accumulateAmount.divide(goalAmountValue, 4, RoundingMode.HALF_UP).doubleValue() * 100)
                .orElse(0.0);
        BigDecimal remainAmount = goalAmount.subtract(accumulateAmount);

        return ProgressResponse.builder()
                .fundingId(funding.getFundingId())
                .progressRate(progressRate)
                .remainAmount(remainAmount)
                .goalAmount(goalAmount)
                .accumulateAmount(accumulateAmount)
                .build();
    }
}
