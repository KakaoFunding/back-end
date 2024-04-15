package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftRepositoryCustom {
    GiftDetailResponse findGiftDetailById(Long giftId);
    GiftDescriptionResponse findGiftDescriptionById(Long giftId);
    Page<GiftResponse> findGiftsByMemberIdAndStatus(Long memberId, GiftStatus status, Pageable pageable);
}
