package org.kakaoshare.backend.domain.order.dto.inquiry.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.order.vo.OrderHistoryDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderHistoryRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    public OrderHistoryDate toDate() {
        return new OrderHistoryDate(startDate, endDate);
    }
}
