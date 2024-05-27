package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
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
    public List<SimpleBrandDto> findAllSimpleBrandByCategoryId(final Long categoryId) {
        BooleanExpression isParent = isEqCategoryId(categoryId).and(category.parent.isNull());
        BooleanExpression condition = conditionByCategoryType(categoryId, isParent);
        
        return queryFactory
                .select(new QSimpleBrandDto(
                        brand.brandId,
                        brand.name,
                        brand.iconPhoto))
                .from(brand)
                .join(brand.products, product)
                .where(condition)
                .distinct()
                .orderBy(getOrderSpecifiers(Pageable.unpaged()))    // TODO: 5/20/24 Pageable 을 전달받지 않아 unpaged()를 넘김 (추후 정렬 관련하여 논의할 때 수정)
                .fetch();
    }

    @Override
    public List<SimpleBrandDto> findBySearchConditions(final String keyword, final Pageable pageable) {
        return queryFactory.select(getSimpleBrandDto())
                .from(brand)
                .innerJoin(product).on(product.brand.eq(brand))
                .where(containsExpression(product.name, keyword))
                .groupBy(brand.brandId)
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
        Long parentCount = Objects.requireNonNull(queryFactory.select(category.countDistinct())
                .from(category)
                .where(isParent)
                .fetchOne());
        
        if (parentCount > 0L) {
            return product.category.parent.categoryId.eq(categoryId);
        }
        return product.category.categoryId.eq(categoryId);
    }
    
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable) {
        return Stream.of(brand.name.asc())
                .toArray(OrderSpecifier[]::new);
    }
}