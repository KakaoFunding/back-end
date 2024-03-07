package org.kakaoshare.backend.domain.category.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.List;
import java.util.Optional;

import static org.kakaoshare.backend.domain.category.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    
    
    @Override
    public Optional<Category> findParentCategoryWithChildren(final Long categoryId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(category)
                        .distinct()
                        .leftJoin(category.children)
                        .fetchJoin()
                        .where(equalCategoryId(categoryId), hasNoParent())
                        .fetchOne());
    }
    
    @Override
    public Optional<Category> findChildCategoryWithParentCheck(final Long categoryId, final Long subcategoryId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(category)
                        .where(equalCategoryId(subcategoryId), equalParentId(categoryId))
                        .fetchOne());
    }
    
    
    @Override
    public List<Category> findAllParentCategories() {
        return queryFactory
                .selectFrom(category)
                .innerJoin(category.children)
                .fetchJoin()
                .where(hasNoParent())
                .fetch();
    }
    
    private static BooleanExpression equalCategoryId(final Long categoryId) {
        return category.categoryId.eq(categoryId);
    }
    
    private static BooleanExpression equalParentId(final Long categoryId) {
        return category.parent.categoryId.eq(categoryId);
    }
    
    private static BooleanExpression hasNoParent() {
        return category.parent.isNull();
    }
}