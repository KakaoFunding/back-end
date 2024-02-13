package org.kakaoshare.backend.domain.category.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.entity.QCategory;

import java.util.List;
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory query;


    @Override
    public List<Category> findAllWithChildren() {
        QCategory category = QCategory.category;
        QCategory child = new QCategory("child");

        return query.selectFrom(category)
                .leftJoin(category.children, child)
                .fetchJoin()
                .distinct()
                .fetch();
    }
}