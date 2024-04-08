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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.receipt.entity.Receipt;

import static org.kakaoshare.backend.domain.order.entity.OrderStatus.COMPLETE_PAYMENT;


@Entity
@Getter
@Table(name = "orders",
        indexes = {
        @Index(name = "idx_orders_receipt_id",columnList = "receipt_id"),
        @Index(name = "idx_orders_funding_detail_id",columnList = "funding_detail_id"),
        @Index(name = "idx_orders_payment_id",columnList = "payment_id",unique = true)
}
)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    @Builder.Default
    @Column(nullable = false,columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = COMPLETE_PAYMENT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_detail_id")
    private FundingDetail fundingDetail;

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

    @Override
    public String toString() {
        return "Order{" +
                "ordersId=" + ordersId +
                ", status=" + status +
                ", fundingDetail=" + fundingDetail +
                ", payment=" + payment +
                ", receipt=" + receipt +
                '}';
    }
}
