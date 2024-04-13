package org.kakaoshare.backend.domain.wish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.WishCancelEvent;
import org.kakaoshare.backend.domain.product.dto.WishEvent;
import org.kakaoshare.backend.domain.wish.dto.WishReservationEvent;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.wish.error.WishErrorCode;
import org.kakaoshare.backend.domain.wish.error.exception.WishException;
import org.kakaoshare.backend.domain.wish.repository.WishRepository;
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
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    
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
        
        if (isRegistered(event, member)) {
            throw new WishException(WishErrorCode.DUPLICATED_WISH);
        }
        
        Wish wish = createWish(event, member)
                .checkIsPublic(event.getType());
        
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
        Member member = getMember(event.getProviderId());
        
        if (!isRegistered(event, member)) {
            throw new WishException(WishErrorCode.NOT_FOUND);
        }
        
        Wish wish = createWish(event, member);
        
        try {
            wishRepository.delete(wish);
        } catch (RuntimeException e) {
            throw new WishException(WishErrorCode.REMOVING_FAILED);
        }
    }
    
    @Recover
    @Transactional
    public void recoverReservation(RuntimeException e, WishReservationEvent event) {
        log.error("wish reservation failed: {}", e.getMessage());
        event.getProduct().decreaseWishCount();
    }
    
    @Recover
    @Transactional
    public void recoverCancel(RuntimeException e, WishCancelEvent event) {
        log.error("wish cancel failed: {}", e.getMessage());
        event.getProduct().increaseWishCount();
    }
    
    private boolean isRegistered(final WishEvent event, final Member member) {
        List<Wish> wishes = member.getWishes();
        return wishes != null && wishes.stream()
                .anyMatch(wish -> wish.getProduct()
                        .getProductId()
                        .equals(event.getProduct().getProductId()));
    }
    
    private Wish createWish(final WishEvent event, final Member member) {
        return Wish.builder()
                .member(member)
                .product(event.getProduct())
                .build();
    }
    
    @Transactional(readOnly = true)
    public Member getMember(final String providerId) {
        return memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }
}
