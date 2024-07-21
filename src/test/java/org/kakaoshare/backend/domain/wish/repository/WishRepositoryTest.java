package org.kakaoshare.backend.domain.wish.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.fixture.WishFixture;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Test
    @DisplayName("위시는 중복된 상품을 기준으로 제외하고 저장한다")
    void testSaveWishIfNotExists() {
        // given
        List<WishFixture> fixtures = Arrays.stream(WishFixture.values()).toList();
        
        List<Product> products = fixtures.stream()
                .map(WishFixture::getProduct)
                .toList();
        
        List<Member> members = fixtures.stream()
                .map(WishFixture::getMember)
                .toList();
        productRepository.saveAllAndFlush(products.stream().distinct().toList());
        memberRepository.saveAllAndFlush(members.stream().distinct().toList());
        
        List<Wish> wishes = fixtures.stream()
                .map(WishFixture::생성)
                .toList();
        
        for (int i = 0; i < wishes.size(); i++) {
            Wish wish = wishes.get(i);
            boolean containInWishList = wishRepository.isContainInWishList(wish, members.get(i), products.get(i).getProductId());
            assertThat(containInWishList).isEqualTo(false);
            wishRepository.saveAndFlush(wish);
            containInWishList = wishRepository.isContainInWishList(wish, members.get(i), products.get(i).getProductId());
            assertThat(containInWishList).isEqualTo(true);
        }
        // then
        
    }
}