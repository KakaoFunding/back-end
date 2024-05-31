package org.kakaoshare.backend.domain.funding.vo;

import org.kakaoshare.backend.common.vo.date.Date;

import java.time.LocalDate;

public class FundingHistoryDate extends Date {
    public FundingHistoryDate(final LocalDate startDate,
                              final LocalDate endDate) {
        super(startDate, endDate);
    }

    private void validateDateRange(final LocalDate startDate, final LocalDate endDate) {
        // TODO: 5/13/24 기여한 펀딩내역 날짜 필터링 시 검증 조건 추가 예정
    }
}
