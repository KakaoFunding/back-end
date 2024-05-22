package org.kakaoshare.backend.domain.cart.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.cart.dto.inquiry.CartResponse;
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

    @Test
    @DisplayName("카트 아이템 등록 - 옵션 선택 포함")
    void registerItemWithOption() {
        Long productId = 1L;
        Long optionId = 1L;
        Long optionDetailId = 1L;
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        Option mockOption = Mockito.mock(Option.class);
        OptionDetail mockOptionDetail = Mockito.mock(OptionDetail.class);

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(mockOption));
        when(optionDetailRepository.findById(optionDetailId)).thenReturn(Optional.of(mockOptionDetail));
        when(cartRepository.findByMemberIdAndProductId(member.getMemberId(), productId)).thenReturn(Optional.empty());

        CartRegisterRequest request = CartRegisterRequest.builder()
                .productId(productId)
                .optionId(optionId)
                .optionDetailId(optionDetailId)
                .build();

        CartRegisterResponse response = cartService.registerItem(request, member.getProviderId());

        assertNotNull(response);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("옵션이 없는 장바구니 아이템 조회")
    void getCartItems() {
        String providerId = "provider123";
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        List<Cart> carts = List.of(new Cart(1L, 2, member, product, null, null));

        when(memberRepository.findMemberByProviderId(providerId)).thenReturn(Optional.of(member));
        when(cartRepository.findByMemberId(member.getMemberId())).thenReturn(carts);

        List<CartResponse> responses = cartService.getCartItems(providerId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(cartRepository).findByMemberId(member.getMemberId());
    }

    @Test
    @DisplayName("옵션이 있는 장바구니 아이템 조회")
    void getCartItemsWithOption() {
        String providerId = "provider123";
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        Option option = new Option(1L, "색상", product);
        OptionDetail optionDetail = new OptionDetail(1L, "빨강", 1, 1000L, "url", option);

        List<Cart> carts = List.of(new Cart(1L, 3, member, product, option, optionDetail));

        when(memberRepository.findMemberByProviderId(providerId)).thenReturn(Optional.of(member));
        when(cartRepository.findByMemberId(member.getMemberId())).thenReturn(carts);

        List<CartResponse> responses = cartService.getCartItems(providerId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(3, responses.get(0).getQuantity());
        assertEquals("색상", responses.get(0).getOptionName());
        assertEquals("빨강", responses.get(0).getOptionDetailName());
        verify(cartRepository).findByMemberId(member.getMemberId());
    }
}
