package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.common.util.RepositoryUtils.containsExpression;
import static org.kakaoshare.backend.common.util.RepositoryUtils.createOrderSpecifiers;
import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@Component
@RequiredArgsConstructor
public class BrandRepositoryCustomImpl implements BrandRepositoryCustom, SortableRepository {
    private final JPAQueryFactory queryFactory;
    
    private BooleanExpression categoryTypeOf(final Long categoryId) {
        BooleanExpression isChildCategory = product.category.categoryId.eq(categoryId);
        return isChildCategory.or(product.category.parent.categoryId.eq(categoryId));
    }
    
    @Override
    public Page<SimpleBrandDto> findAllSimpleBrandByCategoryId(final Long categoryId, final Pageable pageable) {
        Long count = countCategory(categoryId);
        
        if (count == null || count == 0) {
            return Page.empty();
        }
        
        BooleanExpression isParent = isEqCategoryId(categoryId).and(category.parent.isNull());
        
        BooleanExpression condition = isConditionOf(categoryId, isParent);
        
        List<SimpleBrandDto> fetch = queryFactory
                .select(new QSimpleBrandDto(
                        brand.brandId,
                        brand.name,
                        brand.iconPhoto))
                .from(brand)
                .join(brand.products, product)
                .where(condition)
                .distinct()
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        Long totalElement = countTotalElement(condition);
        
        return new PageImpl<>(fetch, pageable, totalElement);
    }
    
    @Nullable
    private Long countTotalElement(final BooleanExpression condition) {
        return queryFactory
                .select(brand.countDistinct())
                .from(brand)
                .join(brand.products, product)
                .where(condition)
                .fetchOne();
    }
    
    private BooleanExpression isConditionOf(final Long categoryId, final BooleanExpression isParent) {
        BooleanExpression condition;
        Long parentCount = queryFactory.select(category.countDistinct())
                .from(category)
                .where(isParent)
                .fetchOne();
        
        if (parentCount > 0L) {
            condition = product.category.parent.categoryId.eq(categoryId);
        } else {
            condition = product.category.categoryId.eq(categoryId);
        }
        return condition;
    }
    
    private Long countCategory(final Long categoryId) {
        return queryFactory.select(category.countDistinct())
                .from(category)
                .where(isEqCategoryId(categoryId))
                .fetchOne();
    }
    
    @Override
    public List<SimpleBrandDto> findBySearchConditions(final String keyword, final Pageable pageable) {
        return queryFactory.select(getSimpleBrandDto())
                .from(brand)
                .leftJoin(product).on(product.brand.eq(brand))
                .where(containsExpression(product.name, keyword))
                .orderBy(createOrderSpecifiers(brand, pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    
    private QSimpleBrandDto getSimpleBrandDto() {
        return new QSimpleBrandDto(
                brand.brandId,
                brand.name,
                brand.iconPhoto);
    }
    
    private BooleanExpression categoryIdEqualTo(final Long categoryId) {
        BooleanExpression isParentCategory = brand.category
                .in(JPAExpressions
                        .select(category)
                        .from(category)
                        .where(category.parent.categoryId.eq(categoryId)));
        
        BooleanExpression isChildCategory = brand.category.categoryId.eq(categoryId);
        
        return isChildCategory.or(isParentCategory);
    }
    
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable) {
        return Stream.of(brand.name.asc())
                .toArray(OrderSpecifier[]::new);
    }
}