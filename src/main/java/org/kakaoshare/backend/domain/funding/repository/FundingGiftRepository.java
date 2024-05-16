package org.kakaoshare.backend.domain.funding.repository;

import org.kakaoshare.backend.domain.gift.entity.FundingGift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingGiftRepository extends JpaRepository<FundingGift, Long> {
}
