package org.kakaoshare.backend.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Redis에 List를 저장할 수 없어 도입한 일급 컬렉션입니다.
 * @author kim-minwoo
 */
@Getter
@NoArgsConstructor
public class OrderDetails {
    private List<OrderDetail> values;

    public OrderDetails(final List<OrderDetail> values) {
        this.values = values;
    }
}
