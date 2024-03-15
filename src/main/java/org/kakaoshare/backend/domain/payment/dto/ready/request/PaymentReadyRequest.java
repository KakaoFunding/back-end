package org.kakaoshare.backend.domain.payment.dto.ready.request;

public record PaymentReadyRequest(Long productId, Integer totalAmount, Integer discountAmount, Integer stockQuantity, String name) {
}
