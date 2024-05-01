package org.kakaoshare.backend.domain.funding.repository;

import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingProductDto;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.repository.query.FundingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FundingRepository extends JpaRepository<Funding, Long>, FundingRepositoryCustom {
    @Query("SELECT NEW org.kakaoshare.backend.domain.funding.dto.preview.request.FundingProductDto(f.goalAmount - f.accumulateAmount, f.product.productId) " +
            "FROM Funding f " +
            "WHERE f.fundingId =:fundingId")
    Optional<FundingProductDto> findAccumulateAmountAndProductIdById(@Param("fundingId") final Long fundingId);
}
