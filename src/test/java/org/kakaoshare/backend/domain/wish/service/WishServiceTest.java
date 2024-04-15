package org.kakaoshare.backend.domain.wish.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.WishCancelEvent;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.product.service.ProductService;
import org.kakaoshare.backend.domain.wish.dto.WishReservationEvent;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.kakaoshare.backend.domain.wish.error.WishErrorCode;
import org.kakaoshare.backend.domain.wish.error.exception.WishException;
import org.kakaoshare.backend.domain.wish.repository.WishRepository;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@RecordApplicationEvents
class WishServiceTest {
    @Autowired
    ApplicationEvents events;
    
    @MockBean
    private ProductRepository productRepository;
    
    Member member;
    Product product;
    @MockBean
    private WishRepository wishRepository;
    
    @Autowired
    private ProductService productService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private WishService wishService;
    
    @BeforeEach
    void setUp() {
        member = MemberFixture.KAKAO.생성();
        product = ProductFixture.TEST_PRODUCT.생성(1L);
    }
    
    @Test
    @Transactional
    @DisplayName("상품 위시 등록 이벤트는 비동기적으로 처리된다.")
    void testWishReservateInAsync() {
        // given
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));
        // when
        productService.resisterProductInWishList(member.getProviderId(), product.getProductId(), WishType.ME);
        WishReservationEvent event = events.stream(WishReservationEvent.class)
                .findFirst()
                .orElseThrow();
        
        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct()).isNotNull();
        assertThat(event.getProviderId()).isEqualTo(member.getProviderId());
        assertThat(event.getType()).isEqualTo(WishType.ME);
    }
    
    @Test
    @Transactional
    @DisplayName("상품 위시 취소 이벤트는 비동기적으로 처리된다.")
    void testWishCancelInAsync() {
        // given
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));
        // when
        productService.removeWishlist(member.getProviderId(), product.getProductId());
        WishCancelEvent event = events.stream(WishCancelEvent.class)
                .findFirst()
                .orElseThrow();
        
        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct()).isNotNull();
        assertThat(event.getProviderId()).isEqualTo(member.getProviderId());
    }
    
    @Test
    @Transactional
    @DisplayName("위시 등록 실패시 상품의 wish count는 복구된다")
    void testReservationFailedRecover() {
        // given
        given(productRepository.findById(product.getProductId()))
                .willReturn(Optional.of(product));
        
        given(memberRepository.findMemberByProviderId(member.getProviderId()))
                .willReturn(Optional.of(member));
        doThrow(new WishException(WishErrorCode.SAVING_FAILED))
                .when(wishRepository)
                .save(any(Wish.class));
        // when
        productService.resisterProductInWishList(member.getProviderId(), product.getProductId(), WishType.ME);
        WishReservationEvent event = events.stream(WishReservationEvent.class)
                .findFirst()
                .orElseThrow();
        // then
        wishService.handleWishReservation(event);
        assertThat(product.getWishCount()).isEqualTo(2);
        wishService.recoverReservation(new WishException(WishErrorCode.SAVING_FAILED), event);
        assertThat(product.getWishCount()).isEqualTo(1);
    }
    
    @Test
    @Transactional
    @DisplayName("위시 삭제 실패시 상품의 wish count는 복구된다")
    void testWishCancelFailed() {
        // given
        given(productRepository.findById(product.getProductId()))
                .willReturn(Optional.of(product));
        
        given(memberRepository.findMemberByProviderId(member.getProviderId()))
                .willReturn(Optional.of(member));
        doThrow(new WishException(WishErrorCode.SAVING_FAILED))
                .when(wishRepository)
                .delete(any(Wish.class));
        // when
        productService.removeWishlist(member.getProviderId(), product.getProductId());
        WishCancelEvent event = events.stream(WishCancelEvent.class)
                .findFirst()
                .orElseThrow();
        
        // then
        wishService.handleWishCancel(event);
        assertThat(product.getWishCount()).isEqualTo(0);
        wishService.recoverCancel(new WishException(WishErrorCode.REMOVING_FAILED), event);
        assertThat(product.getWishCount()).isEqualTo(1);
    }
}