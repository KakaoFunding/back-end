package org.kakaoshare.backend.domain.wish.repository;

import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.wish.repository.query.WishRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WishRepository extends JpaRepository<Wish, Long> , WishRepositoryCustom {
    Optional<Wish> findByMember_ProviderIdAndWishId(final String member_providerId, final Long wishId);
}
