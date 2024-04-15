package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.wish.entity.Wish;

public enum WishFixture {
    TEST_WISH1(ProductFixture.TEST_PRODUCT.가격_설정_생성(5000L), MemberFixture.KAKAO.생성(), true),
    TEST_WISH2(ProductFixture.CAKE.가격_설정_생성(4500L), MemberFixture.KAKAO.생성(), false),
    TEST_WISH3(ProductFixture.COFFEE.가격_설정_생성(8000L), MemberFixture.KAKAO.생성(), false),
    TEST_WISH4(ProductFixture.TEST_PRODUCT.가격_설정_생성(5000L), MemberFixture.KIM.생성(), true),
    TEST_WISH5(ProductFixture.CAKE.가격_설정_생성(4500L), MemberFixture.KIM.생성(), true),
    TEST_WISH6(ProductFixture.COFFEE.가격_설정_생성(8000L), MemberFixture.KIM.생성(), false);
    
    
    private final Product product;
    private final Member member;
    private final boolean isPublic;
    
    WishFixture(Product product, Member member, boolean isPublic) {
        this.product = product;
        this.member = member;
        this.isPublic = isPublic;
    }
    
    public Member getMember() {
        return member;
    }
    
    public Wish 생성(Product product, Member member) {
        return Wish.builder()
                .product(product)
                .member(member)
                .isPublic(isPublic)
                .build();
    }
}
