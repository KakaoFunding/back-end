package org.kakaoshare.backend.domain.payment.dto.ready.request;

import org.kakaoshare.backend.domain.payment.dto.OrderDetail;

import java.util.List;

public record PaymentGiftReadyItem(
        Long productId, Integer totalAmount, Integer discountAmount,
        Integer quantity, List<Long> optionDetailIds) {

    public OrderDetail toOrderDetail(final String orderNumber) {
        return new OrderDetail(orderNumber, productId, quantity, optionDetailIds);
    }
}
