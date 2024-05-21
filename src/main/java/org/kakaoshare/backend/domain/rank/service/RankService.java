package org.kakaoshare.backend.domain.rank.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.common.vo.PriceRange;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.util.RankStrategy;
import org.kakaoshare.backend.domain.rank.util.RankType;
import org.kakaoshare.backend.domain.rank.util.ReceiveRankStrategy;
import org.kakaoshare.backend.domain.rank.util.TargetType;
import org.kakaoshare.backend.domain.rank.util.WishRankStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RankService {
    private final OrderRepository orderRepository;
    private static final int MONTH_VALUE = 6;
    private static final int LIMIT_PRODUCT_COUNT = 20;
    private final Map<RankType, RankStrategy> strategies;

    public RankService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.strategies = new HashMap<>();
        this.strategies.put(RankType.MANY_WISH, new WishRankStrategy(orderRepository));
        this.strategies.put(RankType.MANY_RECEIVE, new ReceiveRankStrategy(orderRepository));
    }

    public Page<RankResponse> getTopRankedProducts(Pageable pageable) {
        return orderRepository.findTopRankedProductsByOrders(LocalDateTime.now().minusMonths(MONTH_VALUE), pageable);
    }

    public List<RankResponse> findProductsByFilters(RankType rankType, TargetType targetType, PriceRange priceRange) {
        RankStrategy strategy = strategies.get(rankType);
        if (strategy == null) {
            return Collections.emptyList();
        }
        return strategy.findProducts(targetType, priceRange, LIMIT_PRODUCT_COUNT);
    }
}
