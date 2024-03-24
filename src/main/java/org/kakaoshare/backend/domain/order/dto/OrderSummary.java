package org.kakaoshare.backend.domain.order.dto;

import org.kakaoshare.backend.domain.order.entity.Order;

import java.math.BigDecimal;

public record OrderSummary(String brandName, String productName, BigDecimal amount, Integer stockQuantity) {
    // TODO: 3/15/24 Order 를 통해 브랜드명, 상품명, 가격, 수량을 가져와야되는 상황. 추후 리펙토링 예정
    public static OrderSummary from(final Order order) {
        return new OrderSummary(
                order.getProduct().getBrand().getName(),
                order.getProduct().getName(),
                order.getProduct().getPrice(),
                order.getStockQuantity()
        );
    }
}
