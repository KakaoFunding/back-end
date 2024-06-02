package org.kakaoshare.backend.domain.rank.util;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.common.vo.PriceRange;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;

@RequiredArgsConstructor
public class ReceiveRankStrategy implements RankStrategy {
    private final OrderRepository orderRepository;

    @Override
    public List<RankResponse> findProducts(TargetType targetType, PriceRange priceRange, int limit) {
        return orderRepository.findProductsByReceived(targetType, priceRange.getMinPrice(), priceRange.getMaxPrice(), limit);
    }
}
