package org.kakaoshare.backend.domain.cart.service;

import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterRequest;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.cart.repository.CartRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.entity.OptionDetail;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.option.repository.OptionRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private OptionDetailRepository optionDetailRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("카트 아이템 등록 - 옵션 선택 없음")
    void registerItemWithoutOption() {
        Long productId = 1L;
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByMemberIdAndProductId(member.getMemberId(), productId)).thenReturn(Optional.empty());

        CartRegisterRequest request = CartRegisterRequest.builder()
                .productId(productId)
                .optionId(null)
                .optionDetailId(null)
                .build();

        CartRegisterResponse response = cartService.registerItem(request, member.getProviderId());

        assertNotNull(response);
        verify(cartRepository).save(any(Cart.class));
    }
}
