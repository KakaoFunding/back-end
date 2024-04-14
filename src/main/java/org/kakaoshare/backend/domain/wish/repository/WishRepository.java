package org.kakaoshare.backend.domain.wish.repository;

import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.wish.repository.query.WishRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WishRepository extends JpaRepository<Wish, Long> , WishRepositoryCustom {
    List<Wish> findByMember_ProviderId(final String providerId);
}
