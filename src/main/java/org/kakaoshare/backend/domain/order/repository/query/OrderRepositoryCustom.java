package org.kakaoshare.backend.domain.order.repository.query;

import java.time.LocalDateTime;
import java.util.List;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;

public interface OrderRepositoryCustom {
    List<RankResponse> findTopRankedProductsByOrders(LocalDateTime sixMonthsAgo);
}
