package org.kakaoshare.backend.domain.brand.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;

@Component
@RequiredArgsConstructor
public class BrandRepositoryCustomImpl implements BrandRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    
    
    @Override
    public List<Brand> findAllByCategoryId(Long categoryId) {
        return queryFactory
                .selectFrom(brand)
                .join(brand.category, category)
                .fetchJoin()
                .where(category.categoryId.eq(categoryId))
                .fetch();
    }
}