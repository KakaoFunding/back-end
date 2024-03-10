package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.QSimpleProductDto;
import org.kakaoshare.backend.domain.product.dto.SimpleProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.product.entity.QProductDetail.productDetail;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom, SortableRepository {
    private final JPAQueryFactory queryFactory;
    
    @Override
    public Page<SimpleProductDto> findAllByCategoryId(final Long categoryId,
                                                      final Pageable pageable) {
        List<SimpleProductDto> fetch = queryFactory
                .select(new QSimpleProductDto(
                        product.productId,
                        product.name,
                        product.photo,
                        product.price,
                        product.brand.name.as("brandName"),
                        product.wishes.size().longValue().as("wishCount")
                ))
                .from(product)
                .where(categoryIdEqualTo(categoryId))
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
    
    @Override
    public DescriptionResponse findProductWithDetailsAndPhotos(Long productId) {
        return queryFactory
                .select(Projections.bean(DescriptionResponse.class,
                        product.name,
                        product.price,
                        product.type,
                        product.photo,
                        product.productDetail.description.as("description"),
                        product.productDescriptionPhotos.as("descriptionPhotos"),
                        product.productDetail.as("hasPhoto"),
                        product.productDetail.productName.as("productName"),
                        product.options,
                        product.brand))
                .from(product)
                .leftJoin(product.productDetail).fetchJoin()
                .leftJoin(product.productDescriptionPhotos).fetchJoin()
                .leftJoin(product.options).fetchJoin()
                .where(product.productId.eq(productId))
                .fetchOne();
    }
    
    @Override
    public DetailResponse findProductDetail(Long productId) {
        return queryFactory
                .select(Projections.constructor(DetailResponse.class,
                        product.productId,
                        product.name,
                        product.price,
                        product.type,
                        product.productDetail.hasPhoto,
                        product.productDetail.productName,
                        product.productDetail.origin,
                        product.productDetail.manufacturer,
                        product.productDetail.tel,
                        product.productDetail.deliverDescription,
                        product.productDetail.billingNotice,
                        product.productDetail.caution,
                        product.options.any(),
                        product.brand))
                .from(product)
                .leftJoin(product.productDetail, productDetail)
                .where(product.productId.eq(productId))
                .fetchOne();
    }
    
    private BooleanExpression categoryIdEqualTo(final Long categoryId) {
        BooleanExpression isParentCategory = product.brand.category
                .in(JPAExpressions
                        .select(category)
                        .from(category)
                        .where(category.parent.categoryId.eq(categoryId)));
        
        BooleanExpression isChildCategory = product.brand.category.categoryId.eq(categoryId);
        
        return isChildCategory.or(isParentCategory);
    }
}
