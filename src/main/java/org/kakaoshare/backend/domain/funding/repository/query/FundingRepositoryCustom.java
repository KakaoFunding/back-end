package org.kakaoshare.backend.domain.funding.repository.query;


import org.kakaoshare.backend.domain.funding.dto.FundingResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface FundingRepositoryCustom {
    Optional<FundingResponse> findByProductIdAndMemberId(Long productId, Long memberId);
    Optional<Funding> findByIdAndMemberId(Long fundingId, Long memberId);

    List<Funding> findAllByMemberId(Long memberId);

    Slice<Funding> findFundingByMemberIdWithSlice(Long memberId, Pageable pageable);
}
