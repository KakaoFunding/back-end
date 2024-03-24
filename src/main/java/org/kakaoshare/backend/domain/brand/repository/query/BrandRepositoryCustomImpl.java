package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.brand.dto.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.category.entity.QCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@Component
@RequiredArgsConstructor
public class BrandRepositoryCustomImpl implements BrandRepositoryCustom,SortableRepository {
    private final JPAQueryFactory queryFactory;
    
    
    private static BooleanExpression isCondition(final Long categoryId) {
        BooleanExpression isChildCategory = product.category.categoryId.eq(categoryId);
        return isChildCategory.or(product.category.parent.categoryId.eq(categoryId));
    }
    
    @Override
    public Page<SimpleBrandDto> findAllSimpleBrandByCategoryId(final Long categoryId, final Pageable pageable) {
        QCategory child = new QCategory("child");
        BooleanExpression isParentCategory = category.parent.categoryId.isNull().and(category.categoryId.eq(categoryId));
        
        List<SimpleBrandDto> fetch = queryFactory
                .select(new QSimpleBrandDto(
                        brand.brandId,
                        brand.name,
                        brand.iconPhoto))
                .from(brand)
                .join(brand.products, product)
                .leftJoin(product.category, child)
                .on(isParentCategory)
                .where(isCondition(categoryId))
                .distinct()
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        Long total =queryFactory
                .select(brand.countDistinct())
                .from(brand)
                .join(brand.products, product)
                .leftJoin(product.category, child)
                .on(isParentCategory)
                .where(isCondition(categoryId))
                .fetchOne();
        
        return new PageImpl<>(fetch, pageable, total);
    }
    
    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(final Pageable pageable) {
        return Stream.of(brand.name.asc())
                .toArray(OrderSpecifier[]::new);
    }
}