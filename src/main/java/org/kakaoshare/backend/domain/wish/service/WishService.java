package org.kakaoshare.backend.domain.wish.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.wish.dto.WishReservationEvent;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.wish.error.WishErrorCode;
import org.kakaoshare.backend.domain.wish.error.exception.WishException;
import org.kakaoshare.backend.domain.wish.repository.WishRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)//TODO 2024 04 11 18:57:19 : 성능 저하 가능성 있음
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Retryable(retryFor = {
            DataAccessException.class,
            DataIntegrityViolationException.class,
            TransactionSystemException.class,
            PersistenceException.class
    }, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public void handleWishReservation(WishReservationEvent event) {
        Member member = getMember(event.getProviderId());
        
        if (isAlreadyRegistered(event, member)) {
            throw new WishException(WishErrorCode.DUPLICATED_WISH);
        }
        
        Wish wish = createWish(event, member);
        
        wishRepository.save(wish);
    }
    
    private boolean isAlreadyRegistered(final WishReservationEvent event, final Member member) {
        return member.getWishes().stream()
                .anyMatch(wish -> wish.getProduct()
                        .getProductId()
                        .equals(event.getProduct().getProductId()));
    }
    
    private Wish createWish(final WishReservationEvent event, final Member member) {
        return Wish.builder()
                .member(member)
                .product(event.getProduct())
                .isPublic(event.getType().isPublic())
                .build();
    }
    
    private Member getMember(final String providerId) {
        return memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }
}
