package org.kakaoshare.backend.common.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import io.jsonwebtoken.lang.Collections;
import org.kakaoshare.backend.common.vo.PriceRange;
import org.kakaoshare.backend.common.vo.date.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public final class RepositoryUtils {
    private RepositoryUtils() {

    }

    public static <T> Page<T> toPage(final Pageable pageable, final JPAQuery<T> contentQuery,
                                     final JPAQuery<Long> countQuery) {
        if (countQuery.fetchFirst() == null) {
            return Page.empty();
        }

        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    public static <T> Page<T> toPage(final Pageable pageable, final List<T> content, final JPAQuery<Long> countQuery) {
        if (countQuery.fetchFirst() == null) {
            return Page.empty();
        }

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public static <T extends ComparableExpression<?>> BooleanExpression eqExpression(
            final SimpleExpression<T> simpleExpression, final T target) {
        if (target == null) {
            return null;
        }

        return simpleExpression.eq(target);
    }

    public static <T extends Enum<T>> BooleanExpression eqExpression(final EnumExpression<T> simpleExpression,
                                                                     final T target) {
        if (target == null) {
            return null;
        }

        return simpleExpression.eq(target);
    }


    public static <T extends Number & Comparable<?>> BooleanExpression eqExpression(final NumberExpression<T> numberExpression, final T target) {
        if (target == null) {
            return null;
        }

        return numberExpression.eq(target);
    }

    public static <T extends ComparableExpression<?>> BooleanExpression eqExpression(final StringExpression stringExpression, final String target) {
        if (target == null) {
            return null;
        }

        return stringExpression.eq(target);
    }

    public static BooleanExpression periodExpression(final DateTimePath<LocalDateTime> dateExpression,
                                                     final Date date) {
        return dateExpression.between(date.getStartDateTime(), date.getEndDateTime());
    }
    public static <T extends Comparable<?>> BooleanExpression eqExpression(final SimpleExpression<T> simpleExpression, final SimpleExpression<T> target) {

        return simpleExpression.eq(target);
    }

    public static BooleanExpression containsExpression(final StringPath stringPath, final String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return stringPath.contains(keyword);
    }

    public static <T extends Number & Comparable<?>> BooleanExpression containsExpression(
            final NumberExpression<T> numberExpression,
            final Integer min,
            final Integer max) {
        if (min == null && max == null) {
            return null;
        }

        if (min == null) {
            return numberExpression.loe(max);
        }

        if (max == null) {
            return numberExpression.goe(min);
        }

        return numberExpression.between(min, max);
    }

    public static <T> BooleanExpression containsExpression(final SimpleExpression<T> simpleExpression,
                                                           final List<T> items) {
        if (Collections.isEmpty(items)) {
            return null;
        }

        return simpleExpression.in(items);
    }


    public static <T> OrderSpecifier<?>[] createOrderSpecifiers(final EntityPathBase<T> qClass, final Pageable pageable) {
        return pageable.getSort()
                .stream()
                .map(sort -> toOrderSpecifier(qClass, sort))
                .toArray(OrderSpecifier[]::new);
    }

    private static <T> OrderSpecifier<?> toOrderSpecifier(final EntityPathBase<T> qClass, final Sort.Order sortOrder) {
        final Order orderMethod = toOrder(sortOrder);
        final PathBuilder<? extends T> pathBuilder = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
        return new OrderSpecifier(orderMethod, pathBuilder.get(sortOrder.getProperty()));
    }

    private static Order toOrder(final Sort.Order sortOrder) {
        if (sortOrder.isAscending()) {
            return Order.ASC;
        }

        return Order.DESC;
    }

    public static <T extends Number & Comparable<?>> BooleanExpression priceExpression(
            final NumberExpression<T> numberExpression, PriceRange priceRange) {
        return numberExpression.between(priceRange.getMinPrice(), priceRange.getMaxPrice());
    }
}
