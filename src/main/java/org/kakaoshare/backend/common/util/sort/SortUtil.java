package org.kakaoshare.backend.common.util.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.kakaoshare.backend.common.util.OrderByNull;
import org.kakaoshare.backend.common.util.sort.error.SortErrorCode;
import org.kakaoshare.backend.common.util.sort.error.exception.UnsupportedSortTypeException;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.springframework.data.domain.Sort.Order;

/**
 * {@link SortableRepository#getOrderSpecifiers(Pageable)}를 구현할 때 기본조건을 제외한 정렬 조건을 {@link #from(Pageable)}로 지정해준다
 * 필요한 정렬 조건을 열거체에 추가하여 사용하시면 됩니다
 * 기본 정렬 조건은 각각의 Repository에서 따로 추가하도록 {@link OrderByNull}을 사용하였고,
 * {@link #from(Pageable)} 뒤에 기본 정렬 조건을 {@link #from(Pageable)}와 합쳐 반환해 {@link com.querydsl.core.support.QueryBase#orderBy(OrderSpecifier)}에 주입하시면 됩니다
 * 기본 정렬 조건이 가장 마지막에 들어가야합니다
 *
 * @author sin-yechan
 * @see SortableRepository
 */
public enum SortUtil {
    PRICE(product.price),
    WISH_COUNT(product.wishCount),
    MOST_RECENT(product.createdAt),
    PRODUCT_NAME(product.name);
    
    private final ComparableExpressionBase<?> expression;
    
    SortUtil(final ComparableExpressionBase<?> expression) {
        this.expression = expression;
    }
    
    
    /**
     * @param pageable {@link Pageable}
     * @return 정렬 조건이 담긴 {@link OrderSpecifier} 배열
     */
    public static OrderSpecifier<?>[] from(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (pageable.getSort().isEmpty()) {
            return new OrderByNull[]{OrderByNull.getDefault()};
        }
        pageable.getSort().forEach(order -> {
            String property = order.getProperty().toUpperCase();
            try {
                SortUtil sortUtil = valueOf(property);
                orderSpecifiers.add(sortUtil.getOrderSpecifiers(order));
            } catch (IllegalArgumentException e) {
                throw new UnsupportedSortTypeException(SortErrorCode.UNSUPPORTED_SORT_TYPE);
            }
        });
        
        
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
    
    private OrderSpecifier<?> getOrderSpecifiers(final Order order) {
        if (order.getDirection().isAscending()) {
            return expression.asc();
        }
        return expression.desc();
    }
}
