package org.kakaoshare.backend.domain.order.repository;

import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.order.repository.query.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
