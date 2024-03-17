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

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int SCALE = 4;
    private static final double DEFAULT_PROGRESS_RATE = 0.0;
    private static final double PERCENT_MULTIPLIER = 100.0;

    private final Long fundingId;
    private final double progressRate;
    private final BigDecimal remainAmount;
    private final BigDecimal goalAmount;
    private final BigDecimal accumulateAmount;

    public static ProgressResponse from(Funding funding) {
        BigDecimal goalAmount = funding.getGoalAmount();
        BigDecimal accumulateAmount = funding.getAccumulateAmount();
        double progressRate = Optional.of(goalAmount)
                .filter(goalAmountValue -> goalAmountValue.compareTo(ZERO) != 0)
                .map(goalAmountValue ->
                        accumulateAmount.divide(goalAmountValue, SCALE, RoundingMode.HALF_UP).doubleValue()
                                * PERCENT_MULTIPLIER)
                .orElse(DEFAULT_PROGRESS_RATE);
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
