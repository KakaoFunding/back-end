package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.RepositoryUtils;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
        
    private BooleanExpression isEqCategoryId(final Long categoryId) {
        return category.categoryId.eq(categoryId);
    }
    
    @Override
    public Page<SimpleBrandDto> findAllSimpleBrandByCategoryId(final Long categoryId, final Pageable pageable) {
        BooleanExpression isParent = isEqCategoryId(categoryId).and(category.parent.isNull());
        
        BooleanExpression condition = conditionByCategoryType(categoryId, isParent);
        
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
        
        JPAQuery<Long> countQuery = countCategory(categoryId);
        
        return RepositoryUtils.toPage(pageable, fetch, countQuery);
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
    
    private BooleanExpression conditionByCategoryType(final Long categoryId, final BooleanExpression isParent) {
        BooleanExpression condition;
        Long parentCount = Objects.requireNonNull(queryFactory.select(category.countDistinct())
                .from(category)
                .where(isParent)
                .fetchOne());
        
        if (parentCount > 0L) {
            condition = product.category.parent.categoryId.eq(categoryId);
        } else {
            condition = product.category.categoryId.eq(categoryId);
        }
        return condition;
    }
    
    private JPAQuery<Long> countCategory(final Long categoryId) {
        return queryFactory.select(category.countDistinct())
                .from(category)
                .where(isEqCategoryId(categoryId));
    }
    
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable) {
        return Stream.of(brand.name.asc())
                .toArray(OrderSpecifier[]::new);
    }
}