package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.dto.QProduct4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom, SortableRepository {
    private final JPAQueryFactory queryFactory;
    
    @Override
    public Page<Product4DisplayDto> findAllByCategoryId(final Long categoryId,
                                                        final Pageable pageable) {
        List<Product4DisplayDto> fetch = queryFactory
                .select(new QProduct4DisplayDto(
                        product.productId,
                        product.name,
                        product.photo,
                        product.price,
                        product.brand.name.as("brandName"),
                        product.wishCount.longValue().as("wishCount")))
                .from(product)
                .where(categoryIdEqualTo(categoryId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(product.countDistinct())
                .from(product)
                .where(categoryIdEqualTo(categoryId))
                .fetchOne();
        return new PageImpl<>(fetch, pageable, total);
    }
    
    @Override
    public Page<ProductDto> findAllByBrandId(final Long brandId,
                                             final Pageable pageable) {
        List<ProductDto> fetch = queryFactory
                .select(new QProductDto(
                        product.productId,
                        product.name,
                        product.photo,
                        product.price)
                )
                .from(product)
                .join(product.brand,brand)
                .where(brand.brandId.eq(brandId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        Long total = queryFactory
                .select(product.countDistinct())
                .from(product)
                .join(product.brand, brand)
                .where(brand.brandId.eq(brandId))
                .fetchOne();
        return new PageImpl<>(fetch, pageable, total);
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
        return null;
//        return queryFactory
//                .select(Projections.bean(DescriptionResponse.class,
//                        product.name,
//                        product.price,
//                        product.type,
//                        product.photo,
//                        product.productDetail.description.as("description"),
//                        product.productDescriptionPhotos.as("descriptionPhotos"),
//                        product.productDetail.as("hasPhoto"),
//                        product.productDetail.productName.as("productName"),
//                        product.options,
//                        product.brand))
//                .from(product)
//                .leftJoin(product.productDetail).fetchJoin()
//                .leftJoin(product.productDescriptionPhotos).fetchJoin()
//                .leftJoin(product.options).fetchJoin()
//                .where(product.productId.eq(productId))
//                .fetchOne();
    }
    
    @Override
    public DetailResponse findProductDetail(Long productId) {
        return null;
//        return queryFactory
//                .select(Projections.constructor(DetailResponse.class,
//                        product.productId,
//                        product.name,
//                        product.price,
//                        product.type,
//                        product.productDetail.hasPhoto,
//                        product.productDetail.productName,
//                        product.productDetail.origin,
//                        product.productDetail.manufacturer,
//                        product.productDetail.tel,
//                        product.productDetail.deliverDescription,
//                        product.productDetail.billingNotice,
//                        product.productDetail.caution,
//                        product.options.any(),
//                        product.brand))
//                .from(product)
//                .leftJoin(product.productDetail, productDetail)
//                .where(product.productId.eq(productId))
//                .fetchOne();
    }
    
    private BooleanExpression categoryIdEqualTo(final Long categoryId) {
        BooleanExpression isParentCategory = product.category
                .in(JPAExpressions
                        .select(category)
                        .from(category)
                        .where(category.parent.categoryId.eq(categoryId)));

        BooleanExpression isChildCategory = product.category.categoryId.eq(categoryId);

        return isChildCategory.or(isParentCategory);
    }
}
