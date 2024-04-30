package org.kakaoshare.backend.domain.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.receipt.entity.Receipt;

import static org.kakaoshare.backend.domain.order.entity.OrderStatus.CANCELLATION_RETURN_EXCHANGE;
import static org.kakaoshare.backend.domain.order.entity.OrderStatus.COMPLETE_PAYMENT;


@Entity
@Getter
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;
    
    @Builder.Default
    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = COMPLETE_PAYMENT;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;
    
    protected Order() {
    }
    
    @Builder
    public Order(final Payment payment, final Receipt receipt) {
        this.payment = payment;
        this.receipt = receipt;
    }

    public void cancel() {
        this.status = CANCELLATION_RETURN_EXCHANGE;
    }

    public boolean canceled() {
        return status.canceled();
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "ordersId=" + ordersId +
                ", status=" + status +
                ", payment=" + payment +
                ", receipt=" + receipt +
                '}';
    }
}
