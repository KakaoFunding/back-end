package org.kakaoshare.backend.domain.funding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingSliceResponse {
    List<FundingResponse> fundingItems;

    private final int numberOfFundingItems;

    private final long page;
    @JsonProperty(value = "isLast")
    private final Boolean isLast;

    public static FundingSliceResponse of(List<FundingResponse> fundingResponses, int numberOfItems, long pageNumber,
                                          boolean last) {
        return FundingSliceResponse.builder()
                .fundingItems(fundingResponses)
                .numberOfFundingItems(numberOfItems)
                .page(pageNumber)
                .isLast(last)
                .build();
    }
}
