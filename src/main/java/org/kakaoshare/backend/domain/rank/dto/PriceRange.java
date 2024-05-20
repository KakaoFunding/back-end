package org.kakaoshare.backend.domain.rank.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PriceRange {
    private int minPrice;
    private int maxPrice;
}
