package org.kakaoshare.backend.domain.order.repository.query;

import java.time.LocalDateTime;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<RankResponse> findTopRankedProductsByOrders(LocalDateTime sixMonthsAgo, Pageable pageable);
}
