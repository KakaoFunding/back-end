package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.entity.QBrand;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.entity.QOption;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.QSimpleProductDto;
import org.kakaoshare.backend.domain.product.dto.SimpleProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.ProductDetail;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.QProductDetail;
import org.kakaoshare.backend.domain.product.entity.QProductThumbnail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.option.entity.QOption.option;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto.productDescriptionPhoto;
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

    public DescriptionResponse findProductWithDetailsAndPhotos(Long productId) {
        Product product = queryFactory
                .selectFrom(QProduct.product)
                .where(QProduct.product.productId.eq(productId))
                .fetchOne();

        List<Option> options = queryFactory
                .selectFrom(QOption.option)
                .where(QOption.option.product.productId.eq(productId))
                .fetch();

        List<ProductThumbnail> thumbnails = queryFactory
                .selectFrom(QProductThumbnail.productThumbnail)
                .where(QProductThumbnail.productThumbnail.product.productId.eq(productId))
                .fetch();

        List<ProductDescriptionPhoto> descriptionPhotos = queryFactory
                .selectFrom(QProductDescriptionPhoto.productDescriptionPhoto)
                .where(QProductDescriptionPhoto.productDescriptionPhoto.product.productId.eq(productId))
                .fetch();

        // 모든 정보를 하나의 DescriptionResponse 객체로 합칩니다.
        return DescriptionResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .brandName(product.getBrand().getName())
                .options(options)
                .productThumbnails(thumbnails)
                .descriptionPhotos(descriptionPhotos)
                .build();
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
