package org.kakaoshare.backend.domain.wish.entity;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;


@Entity
@Getter
@Table(
        indexes = {@Index(name = "idx_wish_product_id", columnList = "product_id", unique = true)}
)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;
    
    @Column(nullable = false)
    private Boolean isPublic;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    public void checkIsPublic(final WishType type) {
        this.isPublic = type.isPublic();
    }
    public boolean equalProductId(final Long productId) {
        return this.product.getProductId() == productId;
    }
    public void changeScopeOfDisclosure(){
        this.isPublic=!this.isPublic;
    }
}
