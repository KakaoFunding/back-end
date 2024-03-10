package org.kakaoshare.backend.domain.member.repository.query;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.kakaoshare.backend.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserDetails> findDetailsByProviderId(final String providerId) {
        return Optional.ofNullable(queryFactory
                .select(getMemberDetailsConstructor(providerId))
                .from(member)
                .where(member.providerId.eq(providerId))
                .fetchOne()
        );
    }

    private ConstructorExpression<MemberDetails> getMemberDetailsConstructor(final String providerId) {
        return Projections.constructor(
                MemberDetails.class,
                Expressions.asString(providerId),
                member.role
        );
    }
}
