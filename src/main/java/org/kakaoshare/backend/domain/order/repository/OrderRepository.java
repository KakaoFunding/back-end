package org.kakaoshare.backend.domain.order.repository;

import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.order.repository.query.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("SELECT o FROM Order o " +
            "WHERE o.payment.paymentId =:paymentId")
    Optional<Order> findByPaymentId(@Param("paymentId") final Long paymentId);
}
