package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftRepositoryCustom {
    Page<GiftResponse> findGifts(Pageable pageable);
}
