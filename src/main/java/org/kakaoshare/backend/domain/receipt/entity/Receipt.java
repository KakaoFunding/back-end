package org.kakaoshare.backend.domain.receipt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(
        indexes = {
                @Index(name = "idx_receipt_product_id", columnList = "product_id", unique = false),
                @Index(name = "idx_receipt_receiver_id", columnList = "receiver_id", unique = false),
                @Index(name = "idx_receipt_recipient_id", columnList = "recipient_id", unique = false),
                @Index(name = "idx_receipt_order_number", columnList = "order_number", unique = true)
        }
)
public class Receipt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Member recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Column(nullable = false)
    private Integer quantity;

    @Builder.Default
    @OneToMany(mappedBy = "receipt", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptOption> options = new ArrayList<>();

    protected Receipt() {

    }

    @Builder
    public Receipt(final String orderNumber,
                   final Product product,
                   final Integer quantity,
                   final Member recipient,
                   final Member receiver,
                   final List<ReceiptOption> options) {
        this.orderNumber = orderNumber;
        this.product = product;
        this.quantity = quantity;
        this.recipient = recipient;
        this.receiver = receiver;
        this.options = options;
        this.options.forEach(option -> option.setReceipt(this));    // TODO: 3/31/24 연관간계 편의 메서드
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId=" + receiptId +
                ", orderNumber='" + orderNumber + '\'' +
                ", product=" + product +
                ", recipient=" + recipient +
                ", receiver=" + receiver +
                ", stockQuantity=" + quantity +
                '}';
    }
}
