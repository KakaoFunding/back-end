package org.kakaoshare.backend.domain.wish.repository.query;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.MyWishDetail;
import org.kakaoshare.backend.domain.wish.entity.Wish;

import java.util.List;

public interface WishRepositoryCustom {
    List<MyWishDetail> findWishDetailsByProviderId(final String providerId);
    List<FriendWishDetail> findWishDetailsByFriendProviderId(final String providerId, final String friendsProviderId);
    boolean isContainInWishList(Wish wish, Member member, Long productId);
}
