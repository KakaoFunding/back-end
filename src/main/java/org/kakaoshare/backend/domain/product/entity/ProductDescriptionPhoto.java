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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {@Index(name = "idx_product_description_photo_product_id",columnList = "product_id",unique = true)}
)
public class ProductDescriptionPhoto extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
