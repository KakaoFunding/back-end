package org.kakaoshare.backend.common.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public abstract class PriceRange {
    private int minPrice;
    private int maxPrice;
    public PriceRange(final int minPrice, final int maxPrice) {
        validatePrice(minPrice, maxPrice);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    private void validatePrice(final int minPrice, final int maxPrice) {
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException();
        }

        if (minPrice < 0) {
            throw new IllegalArgumentException();
        }
    }
}
