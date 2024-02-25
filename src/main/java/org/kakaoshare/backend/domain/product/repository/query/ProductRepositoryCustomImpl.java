package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.entity.query.QSimpleProductDto;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.List;

import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<SimpleProductDto> findAllByCategoryId(final Long categoryId,
                                                      @Nullable Pageable pageable) {
        
        List<SimpleProductDto> fetch = queryFactory
                .select(new QSimpleProductDto(
                        product.name,
                        product.photo,
                        product.price,
                        product.brand.name.as("brandName"),
                        product.wishes.size().longValue().as("wishCount")
                ))
                .from(product)
                .where(product.brand.category.categoryId.eq(8L))
                .orderBy(productSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        return new PageImpl<>(fetch, pageable, fetch.size());
    }
    
    private OrderSpecifier<?> productSort(final Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()){
                    case "price":
                        return new OrderSpecifier<>(direction, product.price);
                    case "wish" :
                        return new OrderSpecifier<>(Order.DESC, product.wishes.size());//위시는 많은 순서로만 정렬
                }
            }
        }
        return new OrderSpecifier<>(Order.ASC, product.name);//기본 정렬은 이름으로
    }
}
