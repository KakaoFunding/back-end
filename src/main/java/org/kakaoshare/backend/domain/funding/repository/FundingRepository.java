package org.kakaoshare.backend.domain.funding.repository;

import java.util.List;
import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingProductDto;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
import org.kakaoshare.backend.domain.funding.repository.query.FundingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FundingRepository extends JpaRepository<Funding, Long>, FundingRepositoryCustom {
    @Query("SELECT NEW org.kakaoshare.backend.domain.funding.dto.preview.request.FundingProductDto(f.goalAmount, f.goalAmount - f.accumulateAmount, f.product.productId) " +
            "FROM Funding f " +
            "WHERE f.fundingId =:fundingId")
    Optional<FundingProductDto> findAccumulateAmountAndProductIdById(@Param("fundingId") final Long fundingId);

    @Query("SELECT f FROM Funding f WHERE f.member.memberId IN :memberIds AND f.status = :status")
    List<Funding> findActiveFundingItemsByMemberIds(List<Long> memberIds, String status);
    @Query("SELECT f FROM Funding f WHERE f.member.memberId = :memberId AND f.status = :status")
    Optional<Funding> findByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") FundingStatus status);
}
