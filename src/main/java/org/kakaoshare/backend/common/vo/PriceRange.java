package org.kakaoshare.backend.common.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public abstract class PriceRange {
    private int minPrice;
    private int maxPrice;
    public PriceRange(final int minPrice, final int maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
