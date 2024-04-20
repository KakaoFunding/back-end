package org.kakaoshare.backend.domain.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RankResponse {
    private Long productId;
    private String productName;
    private Double totalSales;
    private String thumbnailUrl;
}
