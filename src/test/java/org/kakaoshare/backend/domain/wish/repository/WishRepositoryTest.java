//package org.kakaoshare.backend.domain.wish.repository;
//
//import org.junit.jupiter.api.Test;
//import org.kakaoshare.backend.common.RepositoryTest;
//import org.kakaoshare.backend.domain.member.entity.Member;
//import org.kakaoshare.backend.domain.member.repository.MemberRepository;
//import org.kakaoshare.backend.domain.product.entity.Product;
//import org.kakaoshare.backend.domain.product.repository.ProductRepository;
//import org.kakaoshare.backend.domain.wish.dto.WishDetail;
//import org.kakaoshare.backend.domain.wish.entity.Wish;
//import org.kakaoshare.backend.fixture.MemberFixture;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//@RepositoryTest
//class WishRepositoryTest {
//    @Autowired
//    private WishRepository wishRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//
//    @Test
//    void w() {
//        // given
//        Member member = MemberFixture.KAKAO.생성();
//        Member savedMember = memberRepository.saveAndFlush(member);
//        List<Product> products = productRepository.findAll()
//                .stream()
//                .limit(500)
//                .toList();
//
//        List<Wish> wishes = products.stream()
//                .map(product -> Wish.builder()
//                        .member(savedMember)
//                        .product(product)
//                        .isPublic(true).build())
//                .toList();
//        wishRepository.saveAll(wishes);
//
//        // when
////        List<WishDetail> wishDetails = wishRepository.findWishDetailsByProviderId(savedMember.getProviderId());
//        List<WishDetail> wishDetails = wishRepository.findByMember_ProviderId(savedMember.getProviderId())
//                .stream()
//                .map(wish ->
//                        new WishDetail(
//                                wish.getProduct().getProductId(),
//                                wish.getProduct().getName(),
//                                wish.getProduct().getPrice(),
//                                wish.getProduct().getPhoto(),
//                                wish.getIsPublic()
//                        ))
//                .toList();
//        // then
//
//    }
//}