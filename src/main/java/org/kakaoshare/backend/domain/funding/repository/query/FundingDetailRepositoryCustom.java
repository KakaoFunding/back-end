package org.kakaoshare.backend.domain.funding.repository.query;

import org.kakaoshare.backend.common.vo.date.Date;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FundingDetailRepositoryCustom {
    Page<ContributedFundingHistoryDto> findHistoryByCondition(final String providerId, final Date date, final String status, final Pageable pageable);
    Page<ContributedFundingHistoryDto> findHistoryByConditionWithoutStatus(final String providerId, final Date date, final Pageable pageable);
}