package org.kakaoshare.backend.domain.orders.repository;

import org.kakaoshare.backend.domain.orders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
