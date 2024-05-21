package org.kakaoshare.backend.domain.gift.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.receipt.entity.Receipt;

import java.time.LocalDateTime;

import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.CANCEL_REFUND;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.NOT_USED;


@Entity
@Getter
public class Gift extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giftId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,columnDefinition = "varchar(255)")
    private GiftStatus status = NOT_USED;

    @Column
    private String message;

    @Column
    private String messagePhoto;
    
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    protected Gift() {
    }

    @Builder
    public Gift(final LocalDateTime expiredAt, final Receipt receipt) {
        validateExpiredAt(expiredAt);
        this.expiredAt = expiredAt;
        this.receipt = receipt;
    }

    public void cancel() {
        this.status = CANCEL_REFUND;
    }

    public boolean canceled() {
        return status.canceled();
    }

    @Override
    public String toString() {
        return "Gift{" +
                "giftId=" + giftId +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", messagePhoto='" + messagePhoto + '\'' +
                ", expiredAt=" + expiredAt +
                ", receipt=" + receipt +
                '}';
    }

    private void validateExpiredAt(final LocalDateTime expiredAt) {
        if (!expiredAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException();
        }
    }
}
