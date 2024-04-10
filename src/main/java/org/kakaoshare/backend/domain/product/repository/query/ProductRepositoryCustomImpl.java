package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.option.dto.OptionDetailResponse;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.option.dto.ProductOptionDetailResponse;
import org.kakaoshare.backend.domain.option.entity.OptionDetail;
import org.kakaoshare.backend.domain.option.entity.QOption;
import org.kakaoshare.backend.domain.option.entity.QOptionDetail;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.dto.QProduct4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.QProductThumbnail;
import org.kakaoshare.backend.domain.search.dto.QSimpleBrandProductDto;
import org.kakaoshare.backend.domain.search.dto.SimpleBrandProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.kakaoshare.backend.common.util.RepositoryUtils.*;
import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.option.entity.QOption.option;
import static org.kakaoshare.backend.domain.option.entity.QOptionDetail.optionDetail;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto.productDescriptionPhoto;
import static org.kakaoshare.backend.domain.product.entity.QProductDetail.productDetail;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom, SortableRepository {
    private static final int PRODUCT_SIZE_GROUP_BY_BRAND = 9;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product4DisplayDto> findAllByCategoryId(final Long categoryId,
                                                        final Pageable pageable) {
        List<Product4DisplayDto> fetch = queryFactory
                .select(getProduct4DisplayDto())
                .from(product)
                .where(categoryIdEqualTo(categoryId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = countProduct(categoryId);
        return toPage(pageable, fetch, countQuery);
    }

    @Override
    public Page<ProductDto> findAllByBrandId(final Long brandId,
                                             final Pageable pageable) {
        List<ProductDto> fetch = queryFactory
                .select(getProductDto())
                .from(product)
                .join(product.brand, brand)
                .where(brand.brandId.eq(brandId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = countBrand(brandId);
        return toPage(pageable, fetch, countQuery);
    }

    @Override
    public Page<Product4DisplayDto> findBySearchConditions(final String keyword,
                                                           final Integer minPrice,
                                                           final Integer maxPrice,
                                                           final List<String> categories,
                                                           final Pageable pageable) {
        final JPAQuery<Long> countQuery = queryFactory.select(product.productId.count())
                .from(product)
                .where(
                        containsExpression(product.name, keyword),
                        containsExpression(product.price, minPrice, maxPrice)
//                        containsExpression(category.name, categories)
                );

        // TODO: 3/19/24 카테고리 필터링은 추후 구현 예정
        final JPAQuery<Product4DisplayDto> contentQuery = queryFactory.select(getProduct4DisplayDto())
                .from(product)
                .leftJoin(product.brand, brand)
//                .leftJoin(brand.category, category)
                .where(
                        containsExpression(product.name, keyword),
                        containsExpression(product.price, minPrice, maxPrice)
//                        containsExpression(category.name, categories)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(product, pageable));
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<SimpleBrandProductDto> findBySearchConditionsGroupByBrand(final String keyword,
                                                                          final Pageable pageable) {
        final JPAQuery<Long> countQuery = queryFactory.select(product.brand.brandId.countDistinct())
                .from(product)
                .where(containsExpression(product.name, keyword));
        final List<SimpleBrandProductDto> fetch = queryFactory.selectFrom(product)
                .leftJoin(product.brand, brand)
                .where(containsExpression(product.name, keyword))
                .orderBy(createOrderSpecifiers(brand, pageable))
                .offset(pageable.getOffset())
                .transform(groupBy(brand.brandId)
                        .list(new QSimpleBrandProductDto(getSimpleBrandDto(), list(getProduct4DisplayDto())))
                );

        // TODO: 3/21/24 일단은 메모리에서 페이징하는 것으로 구현
        final Map<SimpleBrandDto, List<Product4DisplayDto>> productsGroupByBrand = fetch.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toMap(
                        SimpleBrandProductDto::brand,
                        brandProducts -> brandProducts.products()
                                .subList(0, Math.min(brandProducts.products().size(), PRODUCT_SIZE_GROUP_BY_BRAND))
                ));

        final List<SimpleBrandProductDto> content = productsGroupByBrand.keySet()
                .stream()
                .map(brand -> new SimpleBrandProductDto(brand, productsGroupByBrand.get(brand)))
                .toList();

        return toPage(pageable, content, countQuery);
    }

    @Override
    public Map<Long, Long> findAllPriceByIdsGroupById(final List<Long> productIds) {
        return queryFactory.selectFrom(product)
                .where(containsExpression(product.productId, productIds))
                .transform(
                        groupBy(product.productId)
                                .as(product.price)
                );
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
        // 제품 기본 정보 조회
        Product product = Optional.ofNullable(queryFactory
                        .selectFrom(QProduct.product)
                        .where(QProduct.product.productId.eq(productId))
                        .fetchOne())
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        List<String> descriptionPhotosUrls = queryFactory
                .select(QProductDescriptionPhoto.productDescriptionPhoto.photoUrl)
                .from(QProductDescriptionPhoto.productDescriptionPhoto)
                .where(QProductDescriptionPhoto.productDescriptionPhoto.product.productId.eq(productId))
                .fetch();

        List<OptionResponse> optionsResponses = findOptions(productId);
        List<String> productThumbnailsUrls = queryFactory
                .select(QProductThumbnail.productThumbnail.thumbnailUrl)
                .from(QProductThumbnail.productThumbnail)
                .where(QProductThumbnail.productThumbnail.product.productId.eq(productId))
                .fetch();

        return DescriptionResponse.from(product, descriptionPhotosUrls, optionsResponses, productThumbnailsUrls);
    }


    @Override
    public DetailResponse findProductDetail(Long productId) {
        Product product = queryFactory
                .selectFrom(QProduct.product)
                .where(QProduct.product.productId.eq(productId))
                .fetchOne();

        if (product == null) {
            throw new BusinessException(GlobalErrorCode.RESOURCE_NOT_FOUND);
        }

        List<OptionResponse> optionsResponses = findOptions(productId);
        return DetailResponse.from(product, optionsResponses);
    }


    private QSimpleBrandDto getSimpleBrandDto() {
        return new QSimpleBrandDto(
                brand.brandId,
                brand.name,
                brand.iconPhoto);
    }

    private QProduct4DisplayDto getProduct4DisplayDto() {
        return new QProduct4DisplayDto(
                product.productId,
                product.name,
                product.photo,
                product.price,
                product.brand.name.as("brandName"),
                product.wishCount.longValue().as("wishCount"));
    }

    private QProductDto getProductDto() {
        return new QProductDto(
                product.productId,
                product.name,
                product.photo,
                product.price);
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

    private JPAQuery<Long> countBrand(final Long brandId) {
        return queryFactory
                .select(product.countDistinct())
                .from(product)
                .join(product.brand, brand)
                .where(brand.brandId.eq(brandId));
    }

    private JPAQuery<Long> countProduct(final Long categoryId) {
        return queryFactory
                .select(product.countDistinct())
                .from(product)
                .where(categoryIdEqualTo(categoryId));
    }

    private List<OptionResponse> findOptions(Long productId) {

        // 옵션과 옵션 상세 정보를 조회합니다.
        return queryFactory
                .from(QOption.option)
                .leftJoin(QOptionDetail.optionDetail)
                .on(QOptionDetail.optionDetail.option.optionsId.eq(QOption.option.optionsId))
                .where(QOption.option.product.productId.eq(productId))
                .transform(
                        groupBy(QOption.option.optionsId).list(Projections.constructor(
                                OptionResponse.class,
                                QOption.option.optionsId,
                                QOption.option.name,
                                list(Projections.constructor(
                                        ProductOptionDetailResponse.class,
                                        QOptionDetail.optionDetail.optionDetailId,
                                        QOptionDetail.optionDetail.name,
                                        QOptionDetail.optionDetail.additionalPrice,
                                        QOptionDetail.optionDetail.photo
                                )).as("optionDetails")
                        ))
                );
    }

}
