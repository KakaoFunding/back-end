package org.kakaoshare.backend.domain.gift.repository;

import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.repository.query.GiftRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface GiftRepository extends JpaRepository<Gift, Long>, GiftRepositoryCustom {
    @Query("SELECT g FROM Gift g " +
            "WHERE g.receipt.receiptId =:receiptId")
    Optional<Gift> findByReceiptId(@Param("receiptId") final Long receiptId);
}
