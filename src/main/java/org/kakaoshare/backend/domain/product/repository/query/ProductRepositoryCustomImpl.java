package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.SortUtil;
import org.kakaoshare.backend.common.util.SortableRepository;
import org.kakaoshare.backend.domain.product.entity.query.QSimpleProductDto;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom , SortableRepository {
    private final JPAQueryFactory queryFactory;
    
    @Override
    public Page<SimpleProductDto> findAllByCategoryId(final Long categoryId,
                                                      final Pageable pageable) {
        List<SimpleProductDto> fetch = queryFactory
                .select(new QSimpleProductDto(
                        product.name,
                        product.photo,
                        product.price,
                        product.brand.name.as("brandName"),
                        product.wishes.size().longValue().as("wishCount")
                ))
                .from(product)
                .where(product.brand.category.categoryId.eq(categoryId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        return new PageImpl<>(fetch, pageable, fetch.size());
    }
    
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable) {
        return Stream.concat(
                Stream.of(SortUtil.from(pageable)),
                Stream.of(product.name.asc()) // 기본 정렬 조건
        ).toArray(OrderSpecifier[]::new);
    }
}
