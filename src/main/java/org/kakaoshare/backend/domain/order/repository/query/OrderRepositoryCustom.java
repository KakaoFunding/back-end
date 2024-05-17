package org.kakaoshare.backend.domain.order.repository.query;

import org.kakaoshare.backend.domain.order.dto.inquiry.OrderHistoryDetailDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderProductDto;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepositoryCustom {
    Page<RankResponse> findTopRankedProductsByOrders(LocalDateTime sixMonthsAgo, Pageable pageable);
    Page<OrderProductDto> findAllOrderProductDtoByDate(final LocalDate startDate, final LocalDate endDate, final Pageable pageable);
    Optional<OrderHistoryDetailDto> findHistoryDetailById(final Long orderId);
}
