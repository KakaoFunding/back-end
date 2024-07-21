package org.kakaoshare.backend.domain.funding.repository;

import org.kakaoshare.backend.domain.funding.dto.rank.response.TopContributorResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.kakaoshare.backend.domain.funding.repository.query.FundingDetailRepositoryCustom;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FundingDetailRepository extends JpaRepository<FundingDetail, Long>, FundingDetailRepositoryCustom {
    Optional<FundingDetail> findByFundingAndMember(final Funding funding, final Member member);

    @Query("SELECT fd FROM FundingDetail fd " +
            "WHERE fd.funding.fundingId =:fundingId")
    List<FundingDetail> findAllByFundingId(@Param("fundingId") final Long fundingId);

    @Query("SELECT NEW org.kakaoshare.backend.domain.funding.dto.rank.response.TopContributorResponse(fd.member.profileImageUrl, fd.member.name, fd.rate) " +
            "FROM FundingDetail fd " +
            "LEFT JOIN fd.member m ON m.memberId = fd.member.memberId " +
            "WHERE fd.funding.fundingId = :fundingId")
    Page<TopContributorResponse> findTopContributorsByFundingId(@Param("fundingId") Long fundingId, Pageable pageable);
}
