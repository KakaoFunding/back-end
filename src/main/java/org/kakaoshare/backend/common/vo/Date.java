package org.kakaoshare.backend.common.vo;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
public final class Date {
    private static final int MAX_DATE_PERIOD = 1;

    private final LocalDate startDate;
    private final LocalDate endDate;

    public Date(final LocalDate startDate, final LocalDate endDate) {
        validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDateTime() {
        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDateTime() {
        return endDate.atTime(LocalTime.MAX);
    }

    private void validateDateRange(final LocalDate startDate, final LocalDate endDate) {
        if (startDate == null && endDate == null) {
            throw new IllegalArgumentException();
        }

        if (endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException();
        }

        if (ChronoUnit.YEARS.between(endDate, startDate) >= MAX_DATE_PERIOD) {
            throw new IllegalArgumentException();
        }
    }
}
