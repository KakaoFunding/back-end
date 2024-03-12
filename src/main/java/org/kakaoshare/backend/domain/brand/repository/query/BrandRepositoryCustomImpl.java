package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.entity.query.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;

@Component
@RequiredArgsConstructor
public class BrandRepositoryCustomImpl implements BrandRepositoryCustom,SortableRepository {
    private final JPAQueryFactory queryFactory;
    
    
    @Override
    public Page<SimpleBrandDto> findAllSimpleBrandByCategoryId(final Long categoryId,
                                                            final Pageable pageable) {
        List<SimpleBrandDto> fetch = queryFactory
                .select(new QSimpleBrandDto(
                        brand.brandId,
                        brand.name,
                        brand.iconPhoto))
                .from(brand)
                .where(categoryIdEqualTo(categoryId))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        
        return new PageImpl<>(fetch, pageable, fetch.size());
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