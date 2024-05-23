package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendFundingItemRequest {
    private String friendsProviderId;
    private String accessToken;
}
