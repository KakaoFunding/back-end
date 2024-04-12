package org.kakaoshare.backend.domain.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.product.service.ProductService;
import org.kakaoshare.backend.domain.wish.dto.WishReservationEvent;
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
import static org.mockito.Mockito.when;

@SpringBootTest
@RecordApplicationEvents
class WishServiceTest {
    @Autowired
    ApplicationEvents events;
    
    @MockBean
    private ProductRepository productRepository;
    
    @Autowired
    private ProductService productService;
    
    @Test
    @Transactional(readOnly = true)
    @DisplayName("상품 위시 등록 이벤트는 비동기적으로 처리된다.")
    void testWishReservateInAsync() {
        // given
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));
        // when
        productService.resistProductInWishList(member.getProviderId(),product.getProductId(), WishType.ME);
        WishReservationEvent event = events.stream(WishReservationEvent.class)
                .findFirst()
                .orElseThrow();
        
        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct()).isNotNull();
        assertThat(event.getProviderId()).isEqualTo(member.getProviderId());
        assertThat(event.getType()).isEqualTo(WishType.ME);
    }
}