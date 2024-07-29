package org.kakaoshare.backend.domain.category.repository.query;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.kakaoshare.backend.domain.brand.entity.QBrand.brand;
import static org.kakaoshare.backend.domain.category.entity.QCategory.category;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.dto.QCategoryDto;
import org.kakaoshare.backend.domain.category.dto.QSimpleCategoryDto;
import org.kakaoshare.backend.domain.category.dto.SimpleCategoryDto;
import org.kakaoshare.backend.domain.category.entity.QCategory;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CategoryDto> findParentCategoryWithChildren(final Long categoryId) {
        QCategory child = new QCategory("child");
        return Optional.ofNullable(
                queryFactory
                        .from(category)
                        .leftJoin(category.children, child)
                        .where(category.categoryId.eq(categoryId))
                        .transform(
                                GroupBy.groupBy(category.categoryId)
                                        .as(new QCategoryDto(
                                                category.categoryId,
                                                category.name,
                                                category.parent.categoryId,
                                                GroupBy.list(new QCategoryDto(
                                                        child.categoryId,
                                                        child.name,
                                                        child.parent.categoryId
                                                ))
                                        ))
                        )
                        .get(categoryId));
    }

    @Override
    public Optional<CategoryDto> findChildCategoryWithParentCheck(final Long categoryId, final Long subcategoryId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QCategoryDto(
                                category.categoryId,
                                category.name,
                                category.parent.categoryId
                        ))
                        .from(category)
                        .where(equalCategoryId(subcategoryId), equalParentId(categoryId))
                        .fetchOne());
    }


    @Override
    public List<CategoryDto> findAllParentCategories() {
        QCategory child = new QCategory("child");
        return queryFactory
                .from(category)
                .leftJoin(category.children, child)
                .where(hasNoParent())
                .transform(
                        groupBy(category.categoryId).list(
                                new QCategoryDto(
                                        category.categoryId,
                                        category.name,
                                        category.parent.categoryId,
                                        list(new QCategoryDto(
                                                child.categoryId,
                                                child.name,
                                                child.parent.categoryId
                                        ))
                                )
                        ));
    }

    @Override
    public Long countBrand(final Long categoryId) {
        return queryFactory.select(brand.countDistinct())
                .from(brand)
                .where(brand.products.any().category.parent.categoryId.eq(categoryId))
                .fetchOne();
    }

    @Override
    public Long countProduct(final Long categoryId) {
        return queryFactory.select(product.countDistinct())
                .from(product)
                .where(product.category.parent.categoryId.eq(categoryId))
                .fetchOne();
    }

    @Override
    public List<SimpleCategoryDto> findChildrenCategory(Long categoryId) {
        return queryFactory
                .select(new QSimpleCategoryDto(
                        category.categoryId,
                        category.name
                ))
                .from(category)
                .where(category.parent.categoryId.eq(categoryId)
                        .and(category.children.isEmpty()))
                .fetch();
    }

    private static BooleanExpression equalCategoryId(final Long categoryId) {
        return category.categoryId.eq(categoryId);
    }

    private static BooleanExpression equalParentId(final Long categoryId) {
        return category.parent.categoryId.eq(categoryId);
    }

    private BooleanExpression hasNoParent() {
        return category.parent.isNull();
    }

}