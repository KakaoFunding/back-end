package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.wish.entity.Wish;
public enum WishFixture {
    TEST_WISH1(ProductFixture.TEST_PRODUCT.브랜드_설정_생성(BrandFixture.EDIYA.생성()), MemberFixture.KAKAO.생성(), true, 1L),
    TEST_WISH2(ProductFixture.CAKE.브랜드_설정_생성(BrandFixture.STARBUCKS.생성()), MemberFixture.KAKAO.생성(), false, 2L),
    TEST_WISH3(ProductFixture.COFFEE.브랜드_설정_생성(BrandFixture.EDIYA.생성()), MemberFixture.KAKAO.생성(), false, 3L),
    TEST_WISH4(ProductFixture.TEST_PRODUCT.브랜드_설정_생성(BrandFixture.STARBUCKS.생성()), MemberFixture.KIM.생성(), true, 4L),
    TEST_WISH5(ProductFixture.CAKE.브랜드_설정_생성(BrandFixture.EDIYA.생성()), MemberFixture.KIM.생성(), true, 5L),
    TEST_WISH6(ProductFixture.COFFEE.브랜드_설정_생성(BrandFixture.STARBUCKS.생성()), MemberFixture.KIM.생성(), false, 6L);
    
    
    private final Product product;
    private final Member member;
    private final boolean isPublic;
    private final Long wishId;
    
    WishFixture(Product product, Member member, boolean isPublic, Long wishId) {
        this.product = product;
        this.member = member;
        this.isPublic = isPublic;
        this.wishId = wishId;
    }
    
    public Member getMember() {
        return member;
    }
    
    public Product getProduct() {
        return product;
    }
    public  Wish 생성() {
        return Wish.builder()
                .wishId(wishId)
                .product(product)
                .member(member)
                .isPublic(isPublic)
                .build();
    }
    
    public  Wish 생성(Product product, Member member) {
        return Wish.builder()
                .wishId(wishId)
                .product(product)
                .member(member)
                .isPublic(isPublic)
                .build();
    }
}
