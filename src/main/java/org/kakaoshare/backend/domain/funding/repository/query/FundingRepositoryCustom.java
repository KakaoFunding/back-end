package org.kakaoshare.backend.domain.funding.repository.query;


import java.util.List;
import java.util.Optional;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FundingRepositoryCustom {
    Optional<Funding> findByIdAndMemberId(Long fundingId, Long memberId);

    List<Funding> findAllByMemberId(Long memberId);

    Slice<Funding> findFundingByMemberIdWithSlice(Long memberId, Pageable pageable);
}
