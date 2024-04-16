package org.kakaoshare.backend.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankResponse {
    private Long productId;
    private String productName;
    private Double totalSales;
    private String thumbnailUrl;
}
