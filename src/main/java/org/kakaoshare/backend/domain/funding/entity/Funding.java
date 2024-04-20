package org.kakaoshare.backend.domain.funding.entity;

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
import org.hibernate.annotations.ColumnDefault;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_funding_member_id",columnList = "member_id",unique = true),
                @Index(name = "idx_funding_product_id",columnList = "product_id",unique = true),
        }
)
public class Funding extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingId;
    
    @Column
    private String status;
    
    @Column(nullable = false)
    private LocalDate expiredAt;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private Long goalAmount;
    
    @ColumnDefault("0")
    @Column(nullable = false, precision = 12, scale = 2)
    private Long accumulateAmount = 0L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    public Funding(Member member, Product product, Long goalAmount, LocalDate expiredAt) {
        this.member = member;
        this.product = product;
        this.goalAmount = goalAmount;
        this.expiredAt = expiredAt;
        this.status = "ACTIVE"; // 초기 상태 설정
    }

    public void increaseAccumulateAmount(final Long amount) {
        if (amount != null) {
            this.accumulateAmount += amount;
        }
    }

    @Override
    public String toString() {
        return "Funding{" +
                "fundingId=" + fundingId +
                ", status='" + status + '\'' +
                ", expiredAt=" + expiredAt +
                ", goalAmount=" + goalAmount +
                ", accumulateAmount=" + accumulateAmount +
                ", member=" + member +
                ", product=" + product +
                '}';
    }
}
