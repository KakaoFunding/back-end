package org.kakaoshare.backend.domain.gift.dto.inquiry.history;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.gift.entity.Gift;
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

    public static GiftDto from(final Gift gift) {
        return new GiftDto(
                gift.getGiftId(),
                gift.getExpiredAt(),
                gift.getCreatedAt(),
                gift.getReceipt().getRecipient().getName(),
                gift.getReceipt().getRecipient().getProviderId(),
                ProductDto.from(gift.getReceipt().getProduct())
        );
    }
}
