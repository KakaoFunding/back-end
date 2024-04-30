package org.kakaoshare.backend.domain.payment.repository;

import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentCancelDto;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT NEW org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentCancelDto(p.paymentNumber, p.totalPrice) " +
            "FROM Payment p " +
            "WHERE p.paymentId =:paymentId")
    Optional<PaymentCancelDto> findCancelDtoById(@Param("paymentId") final Long paymentId);
}
