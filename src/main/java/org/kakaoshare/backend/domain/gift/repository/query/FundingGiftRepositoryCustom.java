package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.FundingGiftHistoryResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FundingGiftRepositoryCustom {
    Page<FundingGiftHistoryResponse> findHistoryByCondition(final String providerId, final List<GiftStatus> statuses, final Pageable pageable);
    Page<FundingGiftHistoryResponse> findHistoryByConditionWithoutStatus(final String providerId, final Pageable pageable);
}
