package org.kakaoshare.backend.domain.funding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;

import java.math.BigDecimal;


@Entity
@Getter
public class FundingDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingDetailId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id", nullable = false)
    private Funding funding;


}
