package org.kakaoshare.backend.domain.category.repository.query;

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
    public Optional<Category> findByCategoryIdWithParentAndChildren(final Long categoryId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(category)
                        .distinct()
                        .leftJoin(category.parent)
                        .fetchJoin()
                        .leftJoin(category.children)
                        .fetchJoin()
                        .where(category.categoryId.eq(categoryId))
                        .fetchOne());
    }
    
//    @Override
//    public Optional<Category> findRoot() {
//        Category root = queryFactory
//                .selectFrom(category)
//                .innerJoin(category.children)
//                .fetchJoin()
//                .where(category.parent.isNull())
//                .fetchOne();
//
//        return Optional.ofNullable(root);
//    }
    
    @Override
    public List<Category> findAllParentCategories() {
        return queryFactory
                .selectFrom(category)
                .innerJoin(category.children)
                .fetchJoin()
                .where(category.parent.isNull())
                .fetch();
    }
}