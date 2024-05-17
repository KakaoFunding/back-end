package org.kakaoshare.backend.domain.funding.dto.inquiry.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
public final class ContributedFundingHistoryRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @EnumValue(enumClass = FundingDetailStatus.class, ignoreCase = true, nullable = true)
    private String status;

    public FundingHistoryDate toDate() {
        return new FundingHistoryDate(startDate, endDate);
    }
}
