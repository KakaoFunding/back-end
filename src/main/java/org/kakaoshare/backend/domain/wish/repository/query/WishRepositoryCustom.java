package org.kakaoshare.backend.domain.wish.repository.query;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.wish.dto.WishDetail;
import org.kakaoshare.backend.domain.wish.entity.Wish;

import java.util.List;

public interface WishRepositoryCustom {
    List<WishDetail> findWishDetailsByProviderId(final String providerId);
    boolean isContainInWishList(Wish wish, Member member, Long productId);
}
