package org.kakaoshare.backend.domain.product.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(
        indexes = {
                @Index(name = "idx_product_category_id",columnList = "category_id"),
                @Index(name = "idx_product_brand_id",columnList = "brand_id"),
                @Index(name = "idx_product_product_detail_id",columnList = "product_detail_id",unique = true),
                @Index(name = "idx_product_price",columnList = "price")
        }
)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private Long price;

    @Column(nullable = false, length = 50)
    private String type;

    @Column
    private String photo;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_detail_id", nullable = true)//TODO 2024 02 17 19:45:17 : Detail 관련 로직 작성시 nullable=true 설정
    private ProductDetail productDetail;
    
    @Column(name = "wish_count")
    private Integer wishCount;//TODO 2024 03 21 17:11:42 : pre persist 고려
    
    @Column(name = "order_count")
    private Integer orderCount;//TODO 2024 03 21 17:11:33 : pre persist 고려
    
    @Column(name = "brand_name")
    private String brandName;//TODO 2024 03 21 16:29:28 : pre persist 고려
    
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductThumbnail> productThumbnails;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Cart> carts;
    
    public void increaseWishCount(){
        this.wishCount++;
    }
    public void decreaseWishCount() {
        this.wishCount--;
    }
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", photo='" + photo + '\'' +
                ", wishCount=" + wishCount +
                ", orderCount=" + orderCount +
                ", brandName='" + brandName + '\'' +
                '}';
    }
    public List<String> getThumbnailUrls() {
        if (productThumbnails.isEmpty()) {
            return List.of(photo);
        }

        return productThumbnails.stream()
                .map(ProductThumbnail::getThumbnailUrl)
                .toList();
    }
}
