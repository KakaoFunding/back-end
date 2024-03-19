package org.kakaoshare.backend.domain.funding.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

@Getter
@Builder
public class RegisterRequest {
    private BigDecimal goalAmount;
    private LocalDate expiredAt;

    public Funding toEntity(Member member, Product product){
        return new Funding(member, product, this.goalAmount, this.expiredAt);
    }
}
