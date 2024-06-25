package org.kakaoshare.backend.domain.gift.dto.inquiry.history;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.time.LocalDateTime;

public record GiftDto(Long giftId,
                      LocalDateTime expiredAt,
                      LocalDateTime createdAt,
                      String senderName,
                      String senderProviderId,
                      ProductDto productDto) {

    @QueryProjection
    public GiftDto {
    }
}
