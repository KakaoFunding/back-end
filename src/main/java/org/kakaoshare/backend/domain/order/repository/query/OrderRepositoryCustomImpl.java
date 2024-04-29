package org.kakaoshare.backend.domain.order.repository.query;

import static org.kakaoshare.backend.domain.order.entity.QOrder.order;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.receipt.entity.QReceipt.receipt;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.RepositoryUtils;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public Page<RankResponse> findTopRankedProductsByOrders(LocalDateTime term, Pageable pageable) {
        var subQuery = JPAExpressions
                .select(order.ordersId)
                .from(order)
                .join(order.receipt, receipt)
                .where(order.createdAt.after(term))
                .fetch();

        OrderSpecifier<?>[] orderSpecifiers = RepositoryUtils.createOrderSpecifiers(order, pageable);

        var contentQuery = queryFactory
                .select(Projections.constructor(RankResponse.class,
                        product.productId,
                        product.name,
                        product.price.multiply(order.receipt.quantity).sum().as("totalSales"),
                        product.photo))
                .from(order)
                .join(order.receipt.product, product)
                .where(order.ordersId.in(subQuery))
                .groupBy(product.productId)
                .orderBy(orderSpecifiers);

        var countQuery = queryFactory
                .select(order.ordersId.count())
                .from(order)
                .where(order.ordersId.in(subQuery))
                .groupBy(product.productId);

        return RepositoryUtils.toPage(pageable, contentQuery, countQuery);
    }
}
