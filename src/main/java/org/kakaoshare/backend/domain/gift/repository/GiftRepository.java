package org.kakaoshare.backend.domain.gift.repository;

import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.repository.query.GiftRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GiftRepository extends JpaRepository<Gift, Long>, GiftRepositoryCustom {
}
