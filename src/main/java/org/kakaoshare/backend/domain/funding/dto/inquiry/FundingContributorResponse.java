package org.kakaoshare.backend.domain.funding.dto.inquiry;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.friend.dto.response.Friend;

@Getter
@Builder
public class FundingContributorResponse {
    private String profilePhoto;
    private String profileName;
    private Long contributedAmount;
    private Double contributedPercentage;

    public static FundingContributorResponse of(
            String profilePhoto,
            String profileName,
            Long contributedAmount,
            Double contributionPercentage) {
        return FundingContributorResponse.builder()
                .profilePhoto(profilePhoto)
                .profileName(profileName)
                .contributedAmount(contributedAmount)
                .contributedPercentage(contributionPercentage)
                .build();
    }
}