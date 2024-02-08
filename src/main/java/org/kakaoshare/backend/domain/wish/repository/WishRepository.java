package org.kakaoshare.backend.domain.wish.repository;

import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WishRepository extends JpaRepository<Wish, Long> {
}
