package org.kakaoshare.backend.domain.funding.dto.inquiry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendFundingInquiryRequest {
    private final String friendProviderId;
}
