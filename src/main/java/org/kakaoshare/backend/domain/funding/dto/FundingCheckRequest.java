package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingCheckRequest {
    private String providerId;
}
