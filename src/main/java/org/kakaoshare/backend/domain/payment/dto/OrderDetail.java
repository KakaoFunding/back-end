package org.kakaoshare.backend.domain.payment.dto;

import java.util.List;

/**
 * 결제 준비, 승인 과정에 모두 사용되는 DTO 클래스 입니다.
 * 결제 준비 응답값에 포함되며 추후 승인 과정 요청시 이 값을 Redis에서 꺼내와 Order, Payment, Gift 등 객체를 만드는데 사용합니다.
 * 추후, 할인 기능이 구현되면 이 클래스에 discountAmount 필드를 추가하면 됩니다.
 * @param productId 상품 PK
 * @param quantity 수량
 * @param optionDetailIds 선택한 옵션 상세 PK
 * @author kim-minwoo
 */
public record OrderDetail(String orderNumber, Long productId, Integer quantity, List<Long> optionDetailIds) {
}
