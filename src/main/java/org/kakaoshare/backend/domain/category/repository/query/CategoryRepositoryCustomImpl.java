package org.kakaoshare.backend.domain.category.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.entity.QCategory;

import java.util.List;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static final QCategory category = QCategory.category;
    private static final QCategory child = new QCategory("child");
    
    
    @Override
    public List<Category> findAllWithChildren() {
        return queryFactory.selectFrom(category)
                .leftJoin(category.children, child)
                .fetchJoin()
                .distinct()
                .fetch();
    }
}