package org.kakaoshare.backend.domain.order.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.RepositoryUtils;
import org.kakaoshare.backend.domain.option.dto.QOptionSummaryResponse;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderHistoryDetailDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderProductDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.QOrderProductDto;
import org.kakaoshare.backend.domain.order.vo.OrderHistoryDate;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.kakaoshare.backend.common.util.RepositoryUtils.*;
import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.order.entity.QOrder.order;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.receipt.entity.QReceipt.receipt;
import static org.kakaoshare.backend.domain.receipt.entity.QReceiptOption.receiptOption;

@Component
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
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

    @Override
    public Page<OrderProductDto> findAllOrderProductDtoByCondition(final String providerId, final OrderHistoryDate date, final Pageable pageable) {
        final JPAQuery<Long> countQuery = createOrderProductDtoCountQuery(providerId, date, pageable);
        final JPAQuery<OrderProductDto> contentQuery = createOrderProductDtoContentQuery(providerId, date, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Optional<OrderHistoryDetailDto> findHistoryDetailById(final Long orderId) {
        final OrderHistoryDetailDto orderHistoryDetailDto = queryFactory.select(
                        Projections.constructor(
                                OrderHistoryDetailDto.class,
                                getProductDto(),
                                receipt.quantity,
                                Projections.list(new QOptionSummaryResponse(receiptOption.optionName, receiptOption.optionDetailName))
                        )
                ).from(order)
                .innerJoin(order.receipt, receipt)
                .innerJoin(receipt.product, product)
                .innerJoin(receiptOption).on(eqExpression(receiptOption.receipt.receiptId, receipt.receiptId))
                .where(eqExpression(order.ordersId, orderId))
                .fetchOne();

        return Optional.ofNullable(orderHistoryDetailDto);
    }

    private JPAQuery<?> createOrderProductDtoBaseQuery(final String providerId, final OrderHistoryDate date, final Pageable pageable) {
        return queryFactory.from(order)
                .innerJoin(order.receipt, receipt)
                .innerJoin(receipt.product, product)
                .innerJoin(receipt.receiver, member)
                .where(
                        periodExpression(receipt.createdAt, date),
                        eqExpression(member.providerId, providerId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private JPAQuery<Long> createOrderProductDtoCountQuery(final String providerId, final OrderHistoryDate date, final Pageable pageable) {
        return createOrderProductDtoBaseQuery(providerId, date, pageable)
                .select(order.count());
    }

    private JPAQuery<OrderProductDto> createOrderProductDtoContentQuery(final String providerId, final OrderHistoryDate date, final Pageable pageable) {
        return createOrderProductDtoBaseQuery(providerId, date, pageable)
                .select(getOrderProductDto())
                .orderBy(createOrderSpecifiers(order, pageable));// TODO: 5/8/24 orderBy 내에 값은 추후 변경 예정
    }

    private QOrderProductDto getOrderProductDto() {
        return new QOrderProductDto(
                order.ordersId,
                receipt.orderNumber,
                member.name,
                getProductDto(),
                receipt.quantity,
                order.createdAt,
                order.status.stringValue()
        );
    }

    private QProductDto getProductDto() {
        return new QProductDto(product.productId,
                product.name,
                product.photo,
                product.price,
                product.brandName
        );
    }
}
