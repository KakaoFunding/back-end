package org.kakaoshare.backend.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.hashtag.entity.ProductHashtag;
import org.kakaoshare.backend.domain.theme.entity.Theme;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.orders.entity.Orders;


@Entity
@Getter
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column
    private String photo;

    @Column(nullable = false, length = 50)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "product")
    private List<Option> productOptions;

    @OneToMany(mappedBy = "product")
    private List<ProductHashtag> productProductHashtags;

    @OneToMany(mappedBy = "product")
    private List<Wish> productWishes;

    @OneToMany(mappedBy = "product")
    private List<Theme> productThemes;

    @OneToMany(mappedBy = "product")
    private List<Orders> productOrders;

    @OneToMany(mappedBy = "product")
    private List<Gift> productGifts;

    @OneToMany(mappedBy = "product")
    private List<Funding> productFunding;

    @OneToMany(mappedBy = "product")
    private List<ProductThumbnail> productProductThumbnails;

    @OneToMany(mappedBy = "product")
    private List<ProductDescriptionPhoto> productProductDescriptionPhotos;
}
