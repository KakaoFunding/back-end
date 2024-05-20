package org.kakaoshare.backend.domain.cart.repository;

import java.util.List;
import java.util.Optional;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.cart.repository.query.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart,Long>, CartRepositoryCustom {
    @Query("SELECT c FROM Cart c WHERE c.member.memberId = :memberId AND c.product.productId = :productId")
    Optional<Cart> findByMemberIdAndProductId(@Param("memberId") Long memberId, @Param("productId") Long productId);
    List<Cart> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
