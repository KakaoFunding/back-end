package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftRepositoryCustom {
    Page<GiftResponse> findGiftsByMemberIdAndStatus(Long memberId, GiftStatus status, Pageable pageable);
    Page<GiftResponse> findGiftsByMemberIdAndOtherStatuses(Long memberId, Pageable pageable);
}
