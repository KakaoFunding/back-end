package org.kakaoshare.backend.domain.funding.dto;

import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;

public record RegisterRequest(Long goalAmount, LocalDate expiredAt) {
    public Funding toEntity(Member member, Product product){
        return new Funding(member, product, this.goalAmount, this.expiredAt);
    }
}
