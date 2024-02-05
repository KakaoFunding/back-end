package org.kakaoshare.backend.domain.payment.repository;

import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
