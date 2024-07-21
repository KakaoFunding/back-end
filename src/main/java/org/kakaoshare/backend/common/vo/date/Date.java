package org.kakaoshare.backend.common.vo.date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.kakaoshare.backend.common.vo.date.exception.DateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.kakaoshare.backend.common.vo.date.exception.DateErrorCode.INVALID_END_DATE;
import static org.kakaoshare.backend.common.vo.date.exception.DateErrorCode.INVALID_RANGE;
import static org.kakaoshare.backend.common.vo.date.exception.DateErrorCode.NOT_FOUND_DATE;

@EqualsAndHashCode
@Getter
@ToString
public abstract class Date {
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
        if (startDate == null || endDate == null) {
            throw new DateException(NOT_FOUND_DATE);
        }

        if (endDate.isAfter(LocalDate.now())) {
            throw new DateException(INVALID_END_DATE);
        }

        if (endDate.isBefore(startDate)) {
            throw new DateException(INVALID_RANGE);
        }
    }
}
