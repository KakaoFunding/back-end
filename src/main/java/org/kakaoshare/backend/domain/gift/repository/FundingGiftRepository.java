package org.kakaoshare.backend.domain.gift.repository;

import org.kakaoshare.backend.domain.gift.entity.FundingGift;
import org.kakaoshare.backend.domain.gift.repository.query.FundingGiftRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingGiftRepository extends JpaRepository<FundingGift, Long>, FundingGiftRepositoryCustom {
}
