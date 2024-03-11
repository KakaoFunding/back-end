package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;

@Getter
@Builder
public class RegistrationResponse {
    private final Long id;

    public static RegistrationResponse from(Funding funding) {
        return RegistrationResponse.builder()
                .id(funding.getFundingId())
                .build();
    }
}
