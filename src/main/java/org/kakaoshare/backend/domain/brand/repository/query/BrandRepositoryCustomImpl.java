package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.brand.entity.query.QSimpleBrandDto;
import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;

@Component
@RequiredArgsConstructor
public class BrandRepositoryCustomImpl implements BrandRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    
    
    @Override
    public List<SimpleBrandDto> findAllSimpleBrandByChildId(Long categoryId) {
        return queryFactory
                .select(new QSimpleBrandDto(
                        brand.brandId,
                        brand.name,
                        brand.iconPhoto))
                .from(brand)
                .where(brand.category.categoryId.eq(categoryId))
                .fetch();
    }
    
    @Override
    public List<SimpleBrandDto> findAllSimpleBrandByParentId(final Long categoryId) {
        List<Long> childCategoryIds = queryFactory
                .select(category.categoryId)
                .from(category)
                .where(category.parent.categoryId.eq(categoryId))
                .fetch();
        
        return queryFactory
                .select(new QSimpleBrandDto(
                        brand.brandId,
                        brand.name,
                        brand.iconPhoto))
                .from(brand)
                .where(brand.category.categoryId.in(childCategoryIds))
                .fetch();
    }
}