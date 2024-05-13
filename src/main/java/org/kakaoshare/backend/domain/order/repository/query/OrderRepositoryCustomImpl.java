package org.kakaoshare.backend.domain.order.repository.query;

import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.order.entity.QOrder.order;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.receipt.entity.QReceipt.receipt;
import static org.kakaoshare.backend.domain.wish.entity.QWish.wish;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.RepositoryUtils;
import org.kakaoshare.backend.domain.member.entity.Gender;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.entity.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public List<RankResponse> findProductsByWish(TargetType targetType, int minPrice, int maxPrice, int limit) {

        BooleanExpression genderCondition = createGenderCondition(targetType);
        BooleanExpression priceCondition = product.price.between(minPrice, maxPrice);


        return queryFactory
                .select(Projections.constructor(RankResponse.class,
                        product.productId,
                        product.name,
                        wish.count().as("totalWishes"),
                        product.photo
                ))
                .from(wish)
                .join(wish.product, product)
                .join(wish.member, member)
                .where(genderCondition.and(priceCondition))
                .groupBy(product.productId)
                .orderBy(wish.count().desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression createGenderCondition(TargetType targetType) {
        if (targetType == TargetType.MALE) {
            return member.gender.eq(Gender.MALE);
        } else if (targetType == TargetType.FEMALE) {
            return member.gender.eq(Gender.FEMALE);
        }
        return null;
    }
}
