package org.kakaoshare.backend.domain.rank.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.common.vo.PriceRange;
import org.kakaoshare.backend.domain.rank.dto.RankPriceRange;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.util.RankType;
import org.kakaoshare.backend.domain.rank.util.TargetType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class RankServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RankService rankService;

    @Test
    @DisplayName("랭킹 조회 성공 테스트")
    public void testGetTopRankedProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("totalSales").descending());

        RankResponse rank1 = new RankResponse(1L, "Product A", 15000.0, "thumbnail1.jpg");
        RankResponse rank2 = new RankResponse(2L, "Product B", 7500.0, "thumbnail2.jpg");

        List<RankResponse> rankResponses = Arrays.asList(rank1, rank2);

        Page<RankResponse> page = new PageImpl<>(rankResponses, pageable, rankResponses.size());

        when(orderRepository.findTopRankedProductsByOrders(any(LocalDateTime.class), eq(pageable)))
                .thenReturn(page);
        Page<RankResponse> results = rankService.getTopRankedProducts(pageable);
        assertNotNull(results);
        assertEquals(2, results.getTotalElements());
        assertEquals("Product A", results.getContent().get(0).getProductName());
        assertEquals("Product B", results.getContent().get(1).getProductName());
        verify(orderRepository).findTopRankedProductsByOrders(any(LocalDateTime.class), eq(pageable));
    }

    @Test
    @DisplayName("위시많은순 랭킹 조회")
    public void testFindProductsByFiltersWithWishRankType() {
        List<RankResponse> mockResponses = Arrays.asList(new RankResponse(1L, "Product1", 1000.0, "url1"));
        PriceRange priceRange = new RankPriceRange(0,9999);
        when(orderRepository.findProductsByWish(any(TargetType.class), eq(priceRange.getMinPrice()), eq(priceRange.getMaxPrice()), eq(20)))
                .thenReturn(mockResponses);

        // Execute
        List<RankResponse> results = rankService.findProductsByFilters(RankType.MANY_WISH, TargetType.ALL, priceRange);

        // Verify
        verify(orderRepository).findProductsByWish(eq(TargetType.ALL), eq(0), eq(9999), eq(20));
        assertEquals(mockResponses, results);
    }

    @Test
    @DisplayName("선물많이 받은 순 랭킹 조회")
    public void testFindProductsByFiltersWithReceiveRankType() {
        // Setup
        List<RankResponse> mockResponses = Arrays.asList(new RankResponse(2L, "Product2", 2000.0, "url2"));
        PriceRange priceRange = new RankPriceRange(0,9999);
        when(orderRepository.findProductsByReceived(any(TargetType.class), eq(priceRange.getMinPrice()), eq(priceRange.getMaxPrice()), eq(20)))
                .thenReturn(mockResponses);

        // Execute
        List<RankResponse> results = rankService.findProductsByFilters(RankType.MANY_RECEIVE, TargetType.ALL, priceRange);

        // Verify
        verify(orderRepository).findProductsByReceived(eq(TargetType.ALL), eq(0), eq(9999), eq(20));
        assertEquals(mockResponses, results);
    }


}
