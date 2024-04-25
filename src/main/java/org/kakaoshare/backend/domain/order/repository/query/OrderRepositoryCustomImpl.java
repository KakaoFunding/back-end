package org.kakaoshare.backend.domain.order.repository.query;

import static org.kakaoshare.backend.domain.order.entity.QOrder.order;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.receipt.entity.QReceipt.receipt;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<RankResponse> findTopRankedProductsByOrders(LocalDateTime term) {
        var subQuery = JPAExpressions
                .select(order.ordersId)
                .from(order)
                .join(order.receipt, receipt)
                .where(order.createdAt.after(term))
                .fetch();

        return queryFactory
                .select(Projections.constructor(RankResponse.class,
                        product.productId,
                        product.name,
                        product.price.multiply(order.receipt.quantity).sum().as("totalSales"),
                        product.photo))
                .from(order)
                .join(order.receipt.product, product)
                .where(order.ordersId.in(subQuery))
                .groupBy(product.productId)
                .orderBy(product.price.multiply(order.receipt.quantity).sum().desc())
                .limit(100)
                .fetch();
    }
}
