package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.GiftDto;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftRepositoryCustom {
    GiftDetailResponse findGiftDetailById(Long giftId);
    GiftDescriptionResponse findGiftDescriptionById(Long giftId);
    Page<GiftDto> findHistoryByProviderIdAndStatus(String providerId, GiftStatus status, Pageable pageable);
}
