package org.kakaoshare.backend.domain.gift.repository.query;

import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.FundingGiftHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FundingGiftRepositoryCustom {
    Page<FundingGiftHistoryResponse> findHistoryByCondition(final String providerId, final String status, final Pageable pageable);
    Page<FundingGiftHistoryResponse> findHistoryByConditionWithoutStatus(final String providerId, final Pageable pageable);
}
