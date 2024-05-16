package org.kakaoshare.backend.domain.funding.service;

import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.ContributedFundingHistoryRequest;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FundingDetailService {
    private final FundingDetailRepository fundingDetailRepository;

    public PageResponse<?> lookUp(final String providerId,
                                  final ContributedFundingHistoryRequest contributedFundingHistoryRequest,
                                  final Pageable pageable) {
        final String status = contributedFundingHistoryRequest.getStatus();
        final FundingHistoryDate date = contributedFundingHistoryRequest.toDate();
        final Page<?> page = getFundingDetailHistoryResponse(providerId, date, status, pageable);
        return PageResponse.from(page);
    }

    private Page<?> getFundingDetailHistoryResponse(final String providerId, final FundingHistoryDate date, final String status, final Pageable pageable) {
        if (StringUtils.isNullOrEmpty(status)) {
            return fundingDetailRepository.findHistoryByConditionWithoutStatus(providerId, date, pageable);
        }

        return fundingDetailRepository.findHistoryByCondition(providerId, date, status, pageable);
    }
}
