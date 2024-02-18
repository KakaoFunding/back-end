package org.kakaoshare.backend.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.product.entity.Product;


@Entity
@Getter
public class ProductDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDetailId;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Boolean hasPhoto;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false, length = 20)
    private String tel;

    @Column(nullable = false, columnDefinition = "text")
    private String deliverDescription;

    @Column(nullable = false, columnDefinition = "text")
    private String billingNotice;

    @Column(nullable = false, columnDefinition = "text")
    private String caution;

    @OneToMany(mappedBy = "productDetail")
    private List<Product> products;

}
