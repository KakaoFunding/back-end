package org.kakaoshare.backend.domain.payment.dto;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.product.entity.Product;

/**
 * 결제 준비, 승인 과정에 모두 사용되는 DTO 클래스 입니다.
 * 결제 준비 응답값에 포함되며 추후 승인 과정 요청시 이 값을 프론트로부터 다시 전달받아 Order 객체를 만드는데 사용합니다.
 * @param productId 상품 PK
 * @param stockQuantity 수량
 *
 * @author kim-minwoo
 */
public record PaymentDetail(Long productId, Integer stockQuantity) {
    public Order of(final Member member, final String orderNumber, final Payment payment) {
        return Order.builder()
                .product(new Product(productId))
                .stockQuantity(stockQuantity)
                .orderNumber(orderNumber)
                .member(member)
                .payment(payment)
                .build();
    }
}
