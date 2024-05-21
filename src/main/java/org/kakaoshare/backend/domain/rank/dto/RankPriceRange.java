package org.kakaoshare.backend.domain.rank.dto;

import lombok.Getter;
import org.kakaoshare.backend.common.vo.PriceRange;
@Getter
public class RankPriceRange extends PriceRange {
    public RankPriceRange(int minPrice, int maxPrice) {
        super(minPrice, maxPrice);
    }


}
