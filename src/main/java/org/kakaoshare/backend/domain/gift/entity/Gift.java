package org.kakaoshare.backend.domain.gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.product.entity.Product;


@Entity
@Getter
public class Gift extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giftId;
    @Column(nullable = false, length = 50)
    private String status;

    @Column
    private String message;

    @Column
    private String messagePhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)
    private Order order;
}
