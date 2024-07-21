package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.option.dto.OptionResponse;
import org.kakaoshare.backend.domain.option.dto.ProductOptionDetailResponse;
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
import org.kakaoshare.backend.domain.wish.entity.QWish;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.kakaoshare.backend.common.util.RepositoryUtils.*;
import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.wish.entity.QWish.wish;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom, SortableRepository {
    private static final int PRODUCT_SIZE_GROUP_BY_BRAND = 9;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product4DisplayDto> findAllByCategoryId(final Long categoryId,
                                                        final Pageable pageable,
                                                        final String providerId) {

        JPAQuery<Product4DisplayDto> contentQuery = queryFactory
                .select(getProduct4DisplayDto(providerId))
                .from(product)
                .where(categoryIdEqualTo(categoryId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        JPAQuery<Long> countQuery = countProduct(categoryId);
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<ProductDto> findAllByBrandId(final Long brandId,
                                             final Pageable pageable) {
        JPAQuery<ProductDto> contentQuery = queryFactory
                .select(getProductDto())
                .from(product)
                .join(product.brand, brand)
                .where(brand.brandId.eq(brandId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = countBrand(brandId);
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<ProductDto> findAllByProductIds(final List<Long> productIds, final Pageable pageable) {
        final JPAQuery<Long> countQuery = queryFactory.select(product.productId.count())
                .from(product)
                .where(containsExpression(product.productId, productIds));

        final JPAQuery<ProductDto> contentQuery = queryFactory.select(getProductDto())
                .from(product)
                .where(containsExpression(product.productId, productIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<Product4DisplayDto> findBySearchConditions(final String keyword,
                                                           final Integer minPrice,
                                                           final Integer maxPrice,
                                                           final List<String> categories,
                                                           final Pageable pageable,
                                                           final String providerId) {
        final JPAQuery<Long> countQuery = queryFactory.select(product.productId.count())
                .from(product)
                .where(
                        containsExpression(product.name, keyword),
                        containsExpression(product.price, minPrice, maxPrice)
//                        containsExpression(category.name, categories)
                );

        // TODO: 3/19/24 카테고리 필터링은 추후 구현 예정
        final JPAQuery<Product4DisplayDto> contentQuery = queryFactory.select(getProduct4DisplayDto(providerId))
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
                                                                          final Pageable pageable,
                                                                          final String providerId) {
        final JPAQuery<Long> countQuery = queryFactory.select(product.brand.brandId.countDistinct())
                .from(product)
                .where(containsExpression(product.name, keyword));
        final List<SimpleBrandProductDto> fetch = queryFactory.selectFrom(product)
                .leftJoin(product.brand, brand)
                .where(containsExpression(product.name, keyword))
                .orderBy(createOrderSpecifiers(brand, pageable))
                .offset(pageable.getOffset())
                .transform(groupBy(brand.brandId)
                        .list(new QSimpleBrandProductDto(getSimpleBrandDto(), list(getProduct4DisplayDto(providerId))))
                );

        // TODO: 3/21/24 일단은 메모리에서 페이징하는 것으로 구현
        final Map<SimpleBrandDto, List<Product4DisplayDto>> productsGroupByBrand = fetch.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toMap(
                        SimpleBrandProductDto::brand,
                        brandProducts -> brandProducts.products()
                                .subList(0, Math.min(brandProducts.products().size(), PRODUCT_SIZE_GROUP_BY_BRAND)),
                        (newVal, oldVal) -> newVal
                        //TODO 2024 04 25 15:31:13 : 검색 상품 이름으로 같은 브랜드의 상품들을 조회해 브랜드가 중복되어 조회되는 경우
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
    public Map<Long, String> findAllNameByIdsGroupById(final List<Long> productIds) {
        return queryFactory.selectFrom(product)
                .where(containsExpression(product.productId, productIds))
                .transform(
                        groupBy(product.productId)
                                .as(product.name)
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
    public DescriptionResponse findProductWithDetailsAndPhotosWithoutMember(Product product) {

        List<String> descriptionPhotosUrls = queryFactory
                .select(QProductDescriptionPhoto.productDescriptionPhoto.photoUrl)
                .from(QProductDescriptionPhoto.productDescriptionPhoto)
                .where(QProductDescriptionPhoto.productDescriptionPhoto.product.productId.eq(product.getProductId()))
                .fetch();

        List<OptionResponse> optionsResponses = findOptions(product.getProductId());
        List<String> productThumbnailsUrls = queryFactory
                .select(QProductThumbnail.productThumbnail.thumbnailUrl)
                .from(QProductThumbnail.productThumbnail)
                .where(QProductThumbnail.productThumbnail.product.productId.eq(product.getProductId()))
                .fetch();

        return DescriptionResponse.of(product, descriptionPhotosUrls, optionsResponses, productThumbnailsUrls, false);
    }

    @Override
    public DescriptionResponse findProductWithDetailsAndPhotosWithMember(Product product, Member member) {
        List<String> descriptionPhotosUrls = queryFactory
                .select(QProductDescriptionPhoto.productDescriptionPhoto.photoUrl)
                .from(QProductDescriptionPhoto.productDescriptionPhoto)
                .where(QProductDescriptionPhoto.productDescriptionPhoto.product.productId.eq(product.getProductId()))
                .fetch();

        List<OptionResponse> optionsResponses = findOptions(product.getProductId());
        List<String> productThumbnailsUrls = queryFactory
                .select(QProductThumbnail.productThumbnail.thumbnailUrl)
                .from(QProductThumbnail.productThumbnail)
                .where(QProductThumbnail.productThumbnail.product.productId.eq(product.getProductId()))
                .fetch();

        Boolean isWished = isProductWishedByMember(product.getProductId(), member.getMemberId());

        return DescriptionResponse.of(product, descriptionPhotosUrls, optionsResponses, productThumbnailsUrls, isWished);
    }

    public Product findProductById(Long productId) {
        return queryFactory
                .selectFrom(QProduct.product)
                .where(QProduct.product.productId.eq(productId))
                .fetchOne();
    }

    public DetailResponse findProductDetailWithoutMember(Product product) {
        List<OptionResponse> optionsResponses = findOptions(product.getProductId());

        return DetailResponse.of(product, optionsResponses, false);
    }
    @Override
    public DetailResponse findProductDetailWithMember(Product product, Member member) {
        List<OptionResponse> optionsResponses = findOptions(product.getProductId());

        Boolean isWished = isProductWishedByMember(product.getProductId(), member.getMemberId());

        return DetailResponse.of(product, optionsResponses, isWished);
    }

    private boolean isProductWishedByMember(Long productId, Long memberId) {
        Long wishId = queryFactory
                .select(QWish.wish.wishId)
                .from(QWish.wish)
                .where(QWish.wish.product.productId.eq(productId)
                        .and(QWish.wish.member.memberId.eq(memberId)))
                .fetchFirst();

        return wishId != null;
    }
    private QSimpleBrandDto getSimpleBrandDto() {
        return new QSimpleBrandDto(
                brand.brandId,
                brand.name,
                brand.iconPhoto);
    }

    private QProduct4DisplayDto getProduct4DisplayDto(final String providerId) {
        return new QProduct4DisplayDto(
                product.productId,
                product.name,
                product.photo,
                product.price,
                product.brand.name.as("brandName"),
                product.wishCount.longValue().as("wishCount"),
                isInWishList(providerId));
    }

    private BooleanExpression isInWishList(final String providerId) {
        if (providerId == null) {
            return Expressions.FALSE;
        }
        return JPAExpressions
                .selectOne()
                .from(wish)
                .where(wish.member.providerId.eq(providerId),
                        wish.product.eq(product))
                .exists();
    }

    private QProductDto getProductDto() {
        return new QProductDto(
                product.productId,
                product.name,
                product.photo,
                product.price,
                product.brandName);
    }

    private BooleanExpression categoryIdEqualTo(final Long categoryId) {
        BooleanExpression isParentCategory = product.category
                .in(select(category)
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
        return queryFactory
                .select(Projections.constructor(
                        OptionResponse.class,
                        QOption.option.optionsId,
                        QOption.option.name,
                        GroupBy.list(
                                Projections.constructor(
                                        ProductOptionDetailResponse.class,
                                        QOptionDetail.optionDetail.optionDetailId,
                                        QOptionDetail.optionDetail.name,
                                        QOptionDetail.optionDetail.additionalPrice,
                                        QOptionDetail.optionDetail.photo
                                )
                        )
                ))
                .from(QOption.option)
                .leftJoin(QOptionDetail.optionDetail).on(QOptionDetail.optionDetail.option.optionsId.eq(QOption.option.optionsId))
                .where(QOption.option.product.productId.eq(productId))
                .transform(
                        GroupBy.groupBy(QOption.option.optionsId).list(
                                Projections.constructor(
                                        OptionResponse.class,
                                        QOption.option.optionsId,
                                        QOption.option.name,
                                        GroupBy.list(
                                                Projections.constructor(
                                                        ProductOptionDetailResponse.class,
                                                        QOptionDetail.optionDetail.optionDetailId,
                                                        QOptionDetail.optionDetail.photo,
                                                        QOptionDetail.optionDetail.additionalPrice,
                                                        QOptionDetail.optionDetail.name
                                                )
                                        )
                                )
                        )
                );
    }

}
