package org.kakaoshare.backend.domain.gift.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.gift.entity.Gift;

@Getter
@Builder
public class GiftResponse {
    private final Long giftId;
    private final LocalDateTime expiredAt;
    private final String senderName;
    private final String productName;
    private final String productThumbnail;
    private final String brandName;
    private final LocalDateTime receivedAt;

    public static GiftResponse from(Gift gift){
        return GiftResponse.builder()
                .giftId(gift.getGiftId())
                .expiredAt(gift.getExpiredAt())
                .senderName(gift.getReceipt().getRecipient().getName())
                .productName(gift.getReceipt().getProduct().getName())
                .brandName(gift.getReceipt().getProduct().getBrandName())
                .productThumbnail(gift.getReceipt().getProduct().getProductThumbnails().get(0).getThumbnailUrl())
                .receivedAt(gift.getCreatedAt())
                .build();
    }
}
