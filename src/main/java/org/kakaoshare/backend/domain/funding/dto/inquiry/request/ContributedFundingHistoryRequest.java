package org.kakaoshare.backend.domain.funding.dto.inquiry.request;

import lombok.Getter;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;

@Getter
public final class ContributedFundingHistoryRequest {
    @EnumValue(enumClass = FundingDetailStatus.class, ignoreCase = true, nullable = true)
    private final String status;
    private final FundingHistoryDate date;

    public ContributedFundingHistoryRequest(final String status, final FundingHistoryDate date) {
        this.status = status;
        this.date = date;
    }
}
