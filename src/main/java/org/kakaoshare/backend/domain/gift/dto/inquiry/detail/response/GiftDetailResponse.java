package org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.product.entity.Product;

@Getter
@Builder
public class GiftDetailResponse {
    private final Long giftId;
    private final String message;
    private final String messagePhoto;
    private final LocalDateTime expiredAt;
    private final LocalDateTime createdAt;
    private final GiftStatus status;
    private final Long price;
    private final String giftThumbnail;

    public static GiftDetailResponse of(Gift gift, Product product) {
        return GiftDetailResponse.builder()
                .giftId(gift.getGiftId())
                .message(gift.getMessage())
                .messagePhoto(gift.getMessagePhoto())
                .expiredAt(gift.getExpiredAt())
                .createdAt(product.getCreatedAt())
                .status(gift.getStatus())
                .price(product.getPrice())
                .giftThumbnail(product.getProductThumbnails().get(0).getThumbnailUrl())
                .build();
    }
}
