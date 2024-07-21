package org.kakaoshare.backend.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;

@Entity
@Getter
@Table(indexes = {@Index(name = "idx_product_thumbnail_product_id",columnList = "product_id",unique = true)})
public class ProductThumbnail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productThumbnailId;

    @Column
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
