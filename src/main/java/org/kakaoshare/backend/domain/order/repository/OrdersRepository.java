package org.kakaoshare.backend.domain.order.repository;

import org.kakaoshare.backend.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrdersRepository extends JpaRepository<Order, Long> {
}
