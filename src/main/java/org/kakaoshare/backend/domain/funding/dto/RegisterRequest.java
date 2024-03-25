package org.kakaoshare.backend.domain.funding.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;

@Getter
@Builder
public class RegisterRequest {
    private Long goalAmount;
    private LocalDate expiredAt;

    public Funding toEntity(Member member, Product product){
        return new Funding(member, product, this.goalAmount, this.expiredAt);
    }
}
