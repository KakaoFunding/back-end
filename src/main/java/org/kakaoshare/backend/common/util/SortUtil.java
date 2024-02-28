package org.kakaoshare.backend.common.util;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.springframework.data.domain.Sort.Order;

/**
 * {@link SortableRepository#getOrderSpecifiers(Pageable)}를 구현할 때 기본조건을 제외한 정렬 조건을 {@link #sortBy(Pageable)}로 지정해준다
 * 필요한 정렬 조건을 열거체에 추가하여 사용하시면 됩니다
 * 기본 정렬 조건은 각각의 Repository에서 따로 추가하도록 {@link OrderByNull}을 사용하였고,
 * {@link #sortBy(Pageable)} 뒤에 기본 정렬 조건을 {@link #sortBy(Pageable)}와 합쳐 반환해 {@link com.querydsl.core.support.QueryBase#orderBy(OrderSpecifier)}에 주입하시면 됩니다
 * 기본 정렬 조건이 가장 마지막에 들어가야합니다
 * @author sin-yechan
 * @see org.kakaoshare.backend.common.util.SortableRepository
 */
public enum SortUtil {
    PRICE(product.price),
    WISH_COUNT(product.wishes.size()),
    PRODUCT_NAME(product.name);
    
    private final ComparableExpressionBase<?> expression;
    
    SortUtil(final ComparableExpressionBase<?> expression) {
        this.expression = expression;
    }
    
    
    /**
     * @param  pageable {@link Pageable}
     * @return 정렬 조건이 담긴 {@link OrderSpecifier} 배열
     */
    public static OrderSpecifier<?>[] sortBy(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        pageable.getSort().forEach(order -> {
            String property = order.getProperty().toUpperCase();
            try {
                SortUtil sortUtil = valueOf(property);
                orderSpecifiers.add(sortUtil.getOrderSpecifiers(order));
            } catch (IllegalArgumentException e) {
                //TODO 2024 02 28 18:34:33 : 주어진 속성에 대한 정렬 순서가 열거형에 정의되어 있지 않은 경우
            }
        });
        
        orderSpecifiers.add(OrderByNull.getDefault());// 최종 정렬 조건을 OrderByNull로 주입
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
    
    private OrderSpecifier<?> getOrderSpecifiers(final Order order) {
        return order.getDirection().isAscending()
                ? expression.asc() : expression.desc();
    }
}
