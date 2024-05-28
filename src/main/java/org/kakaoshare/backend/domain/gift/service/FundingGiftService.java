package org.kakaoshare.backend.domain.gift.service;

import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.request.FundingGiftHistoryRequest;
import org.kakaoshare.backend.domain.gift.repository.FundingGiftRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FundingGiftService {
    private final FundingGiftRepository fundingGiftRepository;

    public PageResponse<?> lookUp(final String providerId,
                                  final FundingGiftHistoryRequest fundingGiftHistoryRequest,
                                  final Pageable pageable) {
        final String status = fundingGiftHistoryRequest.getStatus();
        final Page<?> page = getFundingGiftHistoryResponse(providerId, status, pageable);
        return PageResponse.from(page);
    }

    private Page<?> getFundingGiftHistoryResponse(final String providerId,
                                                  final String status,
                                                  final Pageable pageable) {
        if (StringUtils.isNullOrEmpty(status)) {
            return fundingGiftRepository.findHistoryByConditionWithoutStatus(providerId, pageable);
        }

        return fundingGiftRepository.findHistoryByCondition(providerId, status, pageable);
    }
}
