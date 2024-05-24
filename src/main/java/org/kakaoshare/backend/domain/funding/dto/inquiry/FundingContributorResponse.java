package org.kakaoshare.backend.domain.funding.dto.inquiry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingContributorResponse {
    private String profilePhoto;
    private String profileName;
    private Long contributedAmount;
}
