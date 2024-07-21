package org.kakaoshare.backend.domain.hashtag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.product.entity.Product;


@Entity
@Getter
public class ProductHashtag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productHashtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
