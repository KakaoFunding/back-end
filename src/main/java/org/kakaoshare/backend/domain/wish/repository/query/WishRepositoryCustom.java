package org.kakaoshare.backend.domain.wish.repository.query;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.MyWishDetail;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishRepositoryCustom {
    Page<MyWishDetail> findWishDetailsByProviderId(final Pageable pageable, final String providerId);
    List<FriendWishDetail> findWishDetailsByFriendProviderId(final String providerId, final String friendsProviderId);
    boolean isContainInWishList(Wish wish, Member member, Long productId);
}