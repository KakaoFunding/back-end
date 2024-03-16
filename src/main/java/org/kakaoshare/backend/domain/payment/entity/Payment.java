package org.kakaoshare.backend.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;

import java.math.BigDecimal;

import static org.kakaoshare.backend.domain.payment.entity.PaymentMethod.KAKAO_PAY;


@Entity
@Getter
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Builder.Default
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method = KAKAO_PAY;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Builder.Default
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deliveryPrice = BigDecimal.ZERO;

    protected Payment() {
    }

    @Builder
    public Payment(final BigDecimal totalPrice, final BigDecimal purchasePrice) {
        this.totalPrice = totalPrice;
        this.purchasePrice = purchasePrice;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", method=" + method +
                ", totalPrice=" + totalPrice +
                ", purchasePrice=" + purchasePrice +
                ", deliveryPrice=" + deliveryPrice +
                '}';
    }
}
