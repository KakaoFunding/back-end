package org.kakaoshare.backend.domain.wish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.common.util.sort.error.SortErrorCode;
import org.kakaoshare.backend.common.util.sort.error.exception.NoMorePageException;
import org.kakaoshare.backend.domain.friend.service.KakaoFriendService;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoFriendListDto;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.WishCancelEvent;
import org.kakaoshare.backend.domain.product.dto.WishEvent;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.FriendsWishRequest;
import org.kakaoshare.backend.domain.wish.dto.MyWishDetail;
import org.kakaoshare.backend.domain.wish.dto.WishReservationEvent;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.wish.error.WishErrorCode;
import org.kakaoshare.backend.domain.wish.error.exception.WishException;
import org.kakaoshare.backend.domain.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final KakaoFriendService kakaoFriendService;
    
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)//TODO 2024 04 11 18:57:19 : 성능 저하 가능성 있음
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Retryable(
            retryFor = WishException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000),
            recover = "recoverReservation",
            notRecoverable = MemberException.class
    )
    public void handleWishReservation(WishReservationEvent event) {
        Member member = getMember(event.getProviderId());
        Wish wish = createWish(event, member);
        wish.checkIsPublic(event.getType());
        
        if (wishRepository.isContainInWishList(wish, member, event.getProduct().getProductId())) {
            throw new WishException(WishErrorCode.SAVING_FAILED);
        }
        try {
            wishRepository.save(wish);
        } catch (RuntimeException e) {
            throw new WishException(WishErrorCode.SAVING_FAILED);
        }
    }
    
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Retryable(
            retryFor = WishException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000),
            recover = "recoverCancel",
            notRecoverable = MemberException.class
    )
    public void handleWishCancel(WishCancelEvent event) {
        final Member member = getMember(event.getProviderId());
        final Product product = event.getProduct();
        
        try {
            wishRepository.deleteByMemberAndProduct(member, product);
        } catch (RuntimeException e) {
            throw new WishException(WishErrorCode.REMOVING_FAILED);
        }
    }
    
    @Recover
    public void recoverReservation(RuntimeException e, WishReservationEvent event) {
        log.error("wish reservation failed: {}", e.getMessage());
        event.getProduct().decreaseWishCount();
    }
    
    @Recover
    public void recoverCancel(RuntimeException e, WishCancelEvent event) {
        log.error("wish cancel failed: {}", e.getMessage());
        event.getProduct().increaseWishCount();
    }
    
    
    public Member getMember(final String providerId) {
        return memberRepository.findMemberByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }
    
    public PageResponse<?> getMembersWishList(final Pageable pageable,
                                              final String providerId) {
        Page<MyWishDetail> myWishDetails = wishRepository.findWishDetailsByProviderId(pageable, providerId);
        if (myWishDetails.isEmpty()) {
            throw new NoMorePageException(SortErrorCode.NO_MORE_PAGE);
        }
        return PageResponse.from(myWishDetails);
    }
    
    
    public List<FriendWishDetail> getFriendsWishList(final String providerId, final FriendsWishRequest friendsWishRequest) {
        checkIsFriend(friendsWishRequest);
        return wishRepository.findWishDetailsByFriendProviderId(providerId, friendsWishRequest.friendsProviderId());
    }
    
    private void checkIsFriend(final FriendsWishRequest friendsWishRequest) {
        List<KakaoFriendListDto> friends = kakaoFriendService.getFriendsList(friendsWishRequest.kakaoAccessToken());
        boolean isFriend = friends.stream()
                .anyMatch(kakaoFriendListDto -> kakaoFriendListDto
                        .getId()
                        .equals(friendsWishRequest.friendsProviderId()));
        if (!isFriend) {
            throw new MemberException(MemberErrorCode.NO_SUCH_RELATIONSHIP);
        }
    }
    
    private Wish createWish(final WishEvent event, final Member member) {
        return Wish.builder()
                .member(member)
                .product(event.getProduct())
                .build();
    }
    
    @Transactional
    public void changeWishType(final String providerId, final Long wishId) {
        Wish wish = wishRepository.findByMember_ProviderIdAndWishId(providerId, wishId)
                .orElseThrow(() -> new WishException(WishErrorCode.NOT_FOUND));
        wish.changeScopeOfDisclosure();
    }
}