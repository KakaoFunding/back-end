package org.kakaoshare.backend.domain.rank.util;

import java.util.List;
import org.kakaoshare.backend.common.vo.PriceRange;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;

public interface RankStrategy {
    List<RankResponse> findProducts(TargetType targetType, PriceRange priceRange, int limit);
}
