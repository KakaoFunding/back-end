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

import static org.kakaoshare.backend.domain.payment.entity.PaymentMethod.KAKAO_PAY;


@Entity
@Getter
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Builder.Default
    @Column(nullable = false,columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private PaymentMethod method = KAKAO_PAY;

    @Column(nullable = false)
    private String paymentNumber;

    @Column(nullable = false, precision = 12, scale = 2)
    private Long totalPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private Long purchasePrice;

    @Builder.Default
    @Column(nullable = false, precision = 12, scale = 2)
    private Long deliveryPrice = 0L;

    protected Payment() {
    }

    @Builder
    public Payment(final String paymentNumber, final Long totalPrice, final Long purchasePrice) {
        this.paymentNumber = paymentNumber;
        this.totalPrice = totalPrice;
        this.purchasePrice = purchasePrice;
    }

    public void partialCancel(final Long amount) {
        this.totalPrice -= amount;
        this.purchasePrice -= amount;
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
