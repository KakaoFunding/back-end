package org.kakaoshare.backend.domain.order.vo;

import org.kakaoshare.backend.common.vo.date.Date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OrderHistoryDate extends Date {
    private static final int MAX_DATE_PERIOD = 1;

    public OrderHistoryDate(final LocalDate startDate, final LocalDate endDate) {
        super(startDate, endDate);
        validateDateRange(startDate, endDate);
    }

    private void validateDateRange(final LocalDate startDate, final LocalDate endDate) {
        if (ChronoUnit.YEARS.between(endDate, startDate) >= MAX_DATE_PERIOD) {
            throw new IllegalArgumentException();
        }
    }
}
