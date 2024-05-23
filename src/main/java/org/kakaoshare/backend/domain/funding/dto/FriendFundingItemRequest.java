package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;

@Getter
@Builder
public class FriendFundingItemRequest {
    private String friendsProviderId;
    private String accessToken;
    private FundingStatus fundingStatus;
}
