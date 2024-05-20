package org.kakaoshare.backend.domain.rank.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.rank.dto.PriceRange;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.util.RankType;
import org.kakaoshare.backend.domain.rank.util.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RankService {
    private final OrderRepository orderRepository;
    private static final int MONTH_VALUE = 6;
    private static final int LIMIT_PRODUCT_COUNT = 20;

    public Page<RankResponse> getTopRankedProducts(Pageable pageable) {
        return orderRepository.findTopRankedProductsByOrders(LocalDateTime.now().minusMonths(MONTH_VALUE), pageable);
    }

    public List<RankResponse> findProductsByFilters(RankType rankType, TargetType targetType, PriceRange priceRange) {
        if (rankType.equals(RankType.MANY_WISH)) {
            return orderRepository.findProductsByWish(targetType, priceRange.getMinPrice(), priceRange.getMaxPrice(), LIMIT_PRODUCT_COUNT);
        }
        if (rankType.equals(RankType.MANY_RECEIVE)) {
            return orderRepository.findProductsByReceived(targetType, priceRange.getMinPrice(), priceRange.getMaxPrice(), LIMIT_PRODUCT_COUNT);
        }
        return Collections.emptyList();
    }
}
