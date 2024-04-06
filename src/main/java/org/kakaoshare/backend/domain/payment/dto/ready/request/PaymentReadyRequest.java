package org.kakaoshare.backend.domain.payment.dto.ready.request;

import org.kakaoshare.backend.domain.payment.dto.OrderDetail;

import java.util.List;

public record PaymentReadyRequest(
        Long productId, String name, Integer totalAmount, Integer discountAmount,
        Integer stockQuantity, List<Long> optionDetailIds) {

    public OrderDetail toOrderDetail(final String orderNumber) {
        return new OrderDetail(orderNumber, productId, stockQuantity, optionDetailIds);
    }
}
