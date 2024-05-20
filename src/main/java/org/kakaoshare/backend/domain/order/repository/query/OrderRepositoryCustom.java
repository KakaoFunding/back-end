package org.kakaoshare.backend.domain.order.repository.query;

import java.time.LocalDateTime;
import java.util.List;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.util.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<RankResponse> findTopRankedProductsByOrders(LocalDateTime sixMonthsAgo, Pageable pageable);
    List<RankResponse> findProductsByWish(TargetType targetType, int minPrice, int maxPrice, int limit);
    List<RankResponse> findProductsByReceived(TargetType targetType, int minPrice, int maxPrice, int limit);
}
