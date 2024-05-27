package org.kakaoshare.backend.domain.wish.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.MyWishDetail;
import org.kakaoshare.backend.domain.wish.dto.QFriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.QMyWishDetail;
import org.kakaoshare.backend.domain.wish.dto.QWishDetail;
import org.kakaoshare.backend.domain.wish.entity.QWish;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.kakaoshare.backend.common.util.RepositoryUtils.toPage;
import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.wish.entity.QWish.wish;

@Repository
@RequiredArgsConstructor
public class WishRepositoryCustomImpl implements WishRepositoryCustom {
    private static final int FRIEND_WISH_LIMIT = 10;
    private final JPAQueryFactory queryFactory;
    
    @Override
    public Page<MyWishDetail> findWishDetailsByProviderId(final Pageable pageable,
                                                          final String providerId) {
        JPAQuery<Long> countQuery = queryFactory.select(wish.count())
                .from(wish)
                .join(wish.member, member)
                .on(wish.member.providerId.eq(providerId))
                .join(wish.product, product);
        
        JPAQuery<MyWishDetail> contentQuery = queryFactory
                .select(
                        new QMyWishDetail(wish.isPublic,
                                new QWishDetail(
                                        wish.wishId,
                                        product.productId,
                                        product.name,
                                        product.price,
                                        product.photo,
                                        product.brandName,
                                        product.wishCount)))
                .from(wish)
                .join(wish.member, member)
                .on(wish.member.providerId.eq(providerId))
                .join(wish.product, product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        return toPage(pageable, contentQuery, countQuery);
    }
    
    private static BooleanExpression isInMyWishList(final String providerId, final QWish myWish, final QWish friendWish) {
        return JPAExpressions.selectOne()
                .from(myWish)
                .where(myWish.member.providerId.eq(providerId)
                        .and(myWish.product.eq(friendWish.product)))
                .exists();
    }
    
    @Override
    public List<FriendWishDetail> findWishDetailsByFriendProviderId(final String providerId,
                                                                    final String friendsProviderId) {
        QWish friendWish = new QWish("friendWish");
        QWish myWish = new QWish("myWish");
        
        return queryFactory
                .select(
                        new QFriendWishDetail(
                                new QWishDetail(
                                        friendWish.wishId,
                                        product.productId,
                                        product.name,
                                        product.price,
                                        product.photo,
                                        product.brandName,
                                        product.wishCount
                                ),
                                isInMyWishList(providerId, myWish, friendWish)
                        ))
                .from(friendWish)
                .join(friendWish.member, member)
                .on(member.providerId.eq(friendsProviderId)
                        .and(friendWish.isPublic.isTrue()))
                .join(friendWish.product, product)
                //TODO 2024 05 27 20:04:17 : 친구 위시리스트 조회시 정렬 조건 구체화 후 논의
                .orderBy(friendWish.product.wishCount.desc())
                .limit(FRIEND_WISH_LIMIT)
                .fetch();
    }
    
    @Override
    public boolean isContainInWishList(Wish wish, Member member, Long productId) {
        BooleanExpression existsCondition = QWish.wish.member.eq(member)
                .and(QWish.wish.product.productId.eq(productId));
        
        Long count = queryFactory
                .select(QWish.wish.count())
                .from(QWish.wish)
                .where(existsCondition)
                .fetchOne();
        
        return count != null && count > 0;
    }
}