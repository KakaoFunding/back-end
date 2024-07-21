package org.kakaoshare.backend.domain.funding.dto.rank.response;

public record TopContributorResponse(
        String profileUrl,
        String name,
        Double rate
) {
}
