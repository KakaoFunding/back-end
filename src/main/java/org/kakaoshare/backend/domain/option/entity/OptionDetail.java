package org.kakaoshare.backend.domain.option.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(columnList = "option_id")})
public class OptionDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionDetailId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private Long additionalPrice;

    @Column
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

}
