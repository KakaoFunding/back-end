package org.kakaoshare.backend.domain.order.repository.query;

import java.time.LocalDateTime;
import java.util.List;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderHistoryDetailDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderProductDto;
import org.kakaoshare.backend.domain.order.vo.OrderHistoryDate;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.util.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepositoryCustom {
    Page<RankResponse> findTopRankedProductsByOrders(LocalDateTime sixMonthsAgo, Pageable pageable);
    List<RankResponse> findProductsByWish(TargetType targetType, int minPrice, int maxPrice, int limit);
    List<RankResponse> findProductsByReceived(TargetType targetType, int minPrice, int maxPrice, int limit);
    Page<OrderProductDto> findAllOrderProductDtoByCondition(final String providerId, final OrderHistoryDate date, final Pageable pageable);
    Optional<OrderHistoryDetailDto> findHistoryDetailById(final Long orderId);

}
