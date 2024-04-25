package org.kakaoshare.backend.domain.rank.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RankService {
    private final OrderRepository orderRepository;
    private static final int MONTH_VALUE = 6;
    public List<RankResponse> getTopRankedProducts() {
        return orderRepository.findTopRankedProductsByOrders(LocalDateTime.now().minusMonths(MONTH_VALUE));
    }
}
