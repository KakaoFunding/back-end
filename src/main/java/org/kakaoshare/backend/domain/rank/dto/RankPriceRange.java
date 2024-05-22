package org.kakaoshare.backend.domain.rank.dto;

import lombok.Getter;
import org.kakaoshare.backend.common.vo.PriceRange;
import org.kakaoshare.backend.domain.rank.error.RankErrorCode;
import org.kakaoshare.backend.domain.rank.error.exception.RankException;

@Getter
public class RankPriceRange extends PriceRange {
    public RankPriceRange(int minPrice, int maxPrice) {
        super(minPrice, maxPrice);
        validatePriceRange(minPrice, maxPrice);
    }

    private void validatePriceRange(int minPrice, int maxPrice) {
        if (minPrice < 0 || minPrice > maxPrice) {
            throw new RankException(RankErrorCode.INVALID_PRICE_RANGE);
        }
    }
}
