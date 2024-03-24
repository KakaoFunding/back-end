package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;

@Getter
@Builder
public class RegisterResponse {
    private final Long id;

    public static RegisterResponse from(Funding funding) {
        return RegisterResponse.builder()
                .id(funding.getFundingId())
                .build();
    }
}
