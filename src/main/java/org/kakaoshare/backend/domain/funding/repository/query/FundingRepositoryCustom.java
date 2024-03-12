package org.kakaoshare.backend.domain.funding.repository.query;


import java.util.Optional;
import org.kakaoshare.backend.domain.funding.entity.Funding;

public interface FundingRepositoryCustom {
    Optional<Funding> findByIdAndMemberId(Long fundingId, Long memberId);
}
