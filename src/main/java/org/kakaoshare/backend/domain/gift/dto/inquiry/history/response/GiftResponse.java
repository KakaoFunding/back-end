package org.kakaoshare.backend.domain.gift.dto.inquiry.history.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.GiftDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class GiftResponse {
    private final Long giftId;
    private final LocalDateTime expiredAt;
    private final String senderName;
    private final String productName;
    private final String productThumbnail;
    private final String brandName;
    private final Boolean self;
    private final LocalDateTime receivedAt;

    public static GiftResponse of(final GiftDto giftDto,
                                  final String providerId) {
        return new GiftResponse(
                giftDto.giftId(),
                giftDto.expiredAt(),
                giftDto.senderName(),
                giftDto.productDto().getName(),
                giftDto.productDto().getPhoto(),
                giftDto.productDto().getBrandName(),
                giftDto.senderProviderId().equals(providerId),
                giftDto.createdAt()
        );
    }
}
