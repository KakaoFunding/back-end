package org.kakaoshare.backend.domain.wish.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.wish.dto.WishEvent;
import org.kakaoshare.backend.domain.wish.entity.Wish;
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
    
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)//TODO 2024 04 11 18:57:19 : 성능 저하 가능성 있음
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Retryable(retryFor = {
            DataAccessException.class,
            DataIntegrityViolationException.class,
            TransactionSystemException.class,
            PersistenceException.class
    }, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public void handleWishReservation(WishEvent wishEvent) {
        wishRepository.save((Wish) wishEvent.getSource());
    }
}
