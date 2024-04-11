package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;

public interface GiftRepositoryCustom {
    GiftDetailResponse findGiftDetail(Long giftId);
}
