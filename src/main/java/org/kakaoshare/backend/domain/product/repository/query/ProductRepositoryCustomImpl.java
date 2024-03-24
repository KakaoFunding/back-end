package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.entity.QOption;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.dto.QProduct4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.QProductThumbnail;
import org.kakaoshare.backend.domain.search.dto.QSimpleBrandProductDto;
import org.kakaoshare.backend.domain.search.dto.SimpleBrandProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.querydsl.core.group.GroupBy.groupBy;
import static org.kakaoshare.backend.common.util.RepositoryUtils.*;
import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

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

        return new PageImpl<>(fetch, pageable, fetch.size());
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
                .join(product.brand, brand)
                .where(brand.brandId.eq(brandId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(fetch, pageable, fetch.size());
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
    public Page<SimpleBrandProductDto> findBySearchConditionsGroupByBrand(final String keyword, final Pageable pageable) {
        final JPAQuery<Long> countQuery = queryFactory.select(product.brand.brandId.countDistinct())
                .from(product)
                .where(containsExpression(product.name, keyword));
        final List<SimpleBrandProductDto> fetch = queryFactory.selectFrom(product)
                .leftJoin(product.brand, brand)
                .where(containsExpression(product.name, keyword))
                .orderBy(createOrderSpecifiers(brand, pageable))
                .offset(pageable.getOffset())
                .transform(groupBy(brand.brandId)
                        .list(new QSimpleBrandProductDto(getSimpleBrandDto(), GroupBy.list(getProduct4DisplayDto())))
                );

        // TODO: 3/21/24 일단은 메모리에서 페이징하는 것으로 구현
        final Map<SimpleBrandDto, List<Product4DisplayDto>> productsGroupByBrand = fetch.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toMap(
                        SimpleBrandProductDto::brand,
                        brandProducts -> brandProducts.products().subList(0, Math.min(brandProducts.products().size(), PRODUCT_SIZE_GROUP_BY_BRAND))
                ));

        final List<SimpleBrandProductDto> content = productsGroupByBrand.keySet()
                .stream()
                .map(brand -> new SimpleBrandProductDto(brand, productsGroupByBrand.get(brand)))
                .toList();

        return toPage(pageable, content, countQuery);
    }

    @Override
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

        return DetailResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .productName(product.getProductDetail().getProductName())
                .origin(product.getProductDetail().getOrigin())
                .manufacturer(product.getProductDetail().getManufacturer())
                .tel(product.getProductDetail().getTel())
                .deliverDescription(product.getProductDetail().getDeliverDescription())
                .billingNotice(product.getProductDetail().getBillingNotice())
                .caution(product.getProductDetail().getCaution())
                .productThumbnails(thumbnails)
                .options(options)
                .brandName(product.getBrand().getName())
                .build();
    }

    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable) {
        return Stream.concat(
                Stream.of(SortUtil.from(pageable)),
                Stream.of(product.name.asc()) // 기본 정렬 조건
        ).toArray(OrderSpecifier[]::new);
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
                product.wishes.size().longValue().as("wishCount"));
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
