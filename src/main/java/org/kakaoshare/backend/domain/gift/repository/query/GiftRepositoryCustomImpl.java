package org.kakaoshare.backend.domain.gift.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GiftRepositoryCustomImpl implements GiftRepositoryCustom{
    private final JPAQueryFactory queryFactory;
}
