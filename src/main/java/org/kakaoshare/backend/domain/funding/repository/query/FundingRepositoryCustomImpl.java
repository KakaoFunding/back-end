package org.kakaoshare.backend.domain.funding.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FundingRepositoryCustomImpl implements FundingRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}
