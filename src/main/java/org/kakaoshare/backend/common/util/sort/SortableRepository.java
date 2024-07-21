package org.kakaoshare.backend.common.util.sort;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;

/**
 * @implNote  정렬이 필요한 Repository 구현체에 사용
 * @author sin-yechan
 */
public interface SortableRepository {
    OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable);
}
