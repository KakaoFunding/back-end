package org.kakaoshare.backend.domain.cart.entity;

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
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.entity.OptionDetail;
import org.kakaoshare.backend.domain.product.entity.Product;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(
        indexes = {
                @Index(name = "idx_cart_member_id", columnList = "member_id"),
                @Index(name = "idx_cart_product_id", columnList = "product_id")
        }
)
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(nullable = false)
    private int itemCount;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isSelected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_detail_id")
    private OptionDetail optionDetail;

    public void updateItemCount(int additionalCount) {
        this.itemCount += additionalCount;
    }

    public Long calculateTotalPrice() {
        Long productPrice = product != null ? product.getPrice() : 0L;
        Long additionalPrice = optionDetail != null ? optionDetail.getAdditionalPrice() : 0L;
        return itemCount * (productPrice + additionalPrice);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", itemCount=" + itemCount +
                ", member=" + member +
                ", product=" + product +
                ", option=" + option +
                ", optionDetail=" + optionDetail +
                ", isSelected=" + isSelected +
                '}';
    }

    public void changeIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
