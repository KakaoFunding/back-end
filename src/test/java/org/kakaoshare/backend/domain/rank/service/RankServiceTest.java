package org.kakaoshare.backend.domain.rank.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class RankServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RankService rankService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10, Sort.by("totalSales").descending());

        RankResponse rank1 = new RankResponse(1L, "Product A", 15000.0, "thumbnail1.jpg");
        RankResponse rank2 = new RankResponse(2L, "Product B", 7500.0, "thumbnail2.jpg");
        List<RankResponse> rankResponses = Arrays.asList(rank1, rank2);

        when(orderRepository.findTopRankedProductsByOrders(any(LocalDateTime.class), eq(pageable)))
                .thenReturn(rankResponses);
    }

    @Test
    @DisplayName("랭킹 조회 성공 테스트")
    public void testGetTopRankedProducts() {
        List<RankResponse> results = rankService.getTopRankedProducts(pageable);
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Product A", results.get(0).getProductName());
        assertEquals("Product B", results.get(1).getProductName());
        verify(orderRepository).findTopRankedProductsByOrders(any(LocalDateTime.class), eq(pageable));
    }
}
