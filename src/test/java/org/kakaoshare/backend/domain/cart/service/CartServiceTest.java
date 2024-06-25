package org.kakaoshare.backend.domain.cart.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.cart.dto.inquiry.CartItemCountResponse;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
    @Captor
    private ArgumentCaptor<Cart> cartCaptor;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("카트 아이템 등록 - 옵션 선택 없음")
    void registerItemWithoutOption() {
        Long productId = 1L;
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        Option defaultOption = new Option(1L, "Default Option", product);
        OptionDetail defaultOptionDetail = new OptionDetail(1L, "Default Detail", 10, 1000L, null, defaultOption);

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.findByProductId(productId)).thenReturn(List.of(defaultOption)); // 기본 옵션 설정
        when(optionDetailRepository.findByOptionId(defaultOption.getOptionsId())).thenReturn(
                List.of(defaultOptionDetail)); // 기본 옵션 상세 설정
        when(cartRepository.findByMemberIdAndProductId(member.getMemberId(), productId)).thenReturn(Optional.empty());

        CartRegisterRequest request = CartRegisterRequest.builder()
                .productId(productId)
                .optionId(null)
                .optionDetailId(null)
                .build();

        CartRegisterResponse response = cartService.registerItem(request, member.getProviderId());

        verify(cartRepository).save(cartCaptor.capture());
        Cart savedCart = cartCaptor.getValue();

        assertNotNull(savedCart.getOption());
        assertNotNull(savedCart.getOptionDetail());
        assertNotNull(response);
    }


    @Test
    @DisplayName("카트 아이템 등록 - 옵션 선택 포함")
    void registerItemWithOption() {
        Long productId = 1L;
        Long optionId = 1L;
        Long optionDetailId = 1L;
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        Option defaultOption = new Option(1L, "Default Option", product);
        OptionDetail defaultOptionDetail = new OptionDetail(1L, "Default Detail", 10, 1000L, null, defaultOption);

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(defaultOption));
        when(optionDetailRepository.findById(optionDetailId)).thenReturn(Optional.of(defaultOptionDetail));
        when(cartRepository.findByMemberIdAndProductId(member.getMemberId(), productId)).thenReturn(Optional.empty());

        CartRegisterRequest request = CartRegisterRequest.builder()
                .productId(productId)
                .optionId(optionId)
                .optionDetailId(optionDetailId)
                .build();

        CartRegisterResponse response = cartService.registerItem(request, member.getProviderId());

        verify(cartRepository).save(cartCaptor.capture());
        Cart savedCart = cartCaptor.getValue();

        assertNotNull(savedCart.getOption());
        assertNotNull(savedCart.getOptionDetail());
        assertNotNull(response);
    }

    @Test
    @DisplayName("옵션이 없는 장바구니 아이템 조회")
    void getCartItems() {
        String providerId = "provider123";
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        List<Cart> carts = List.of(new Cart(1L, 2, true, member, product, null, null));

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

        List<Cart> carts = List.of(new Cart(1L, 3, true, member, product, option, optionDetail));

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

    @Test
    @DisplayName("장바구니 아이템 수량 수정")
    void updateCartItem() {
        Long productId = 1L;
        String providerId = "provider123";
        int newQuantity = 3;

        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        Cart cart = new Cart(1L, 2, true, member, product, null, null);

        when(memberRepository.findMemberByProviderId(providerId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByMemberIdAndProductId(member.getMemberId(), productId)).thenReturn(Optional.of(cart));

        cartService.updateItem(productId, providerId, newQuantity);

        assertEquals(newQuantity, cart.getItemCount());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("장바구니 아이템 삭제")
    void deleteCartItem() {
        Long productId = 1L;
        String providerId = "provider123";

        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L);
        Cart cart = new Cart(1L, 2, true, member, product, null, null);

        when(memberRepository.findMemberByProviderId(providerId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByMemberIdAndProductId(member.getMemberId(), productId)).thenReturn(Optional.of(cart));

        cartService.deleteItem(productId, providerId);

        verify(cartRepository).delete(cart);
    }

    @Test
    @DisplayName("장바구니 아이템 수 조회")
    void getCartItemCount() {
        String providerId = "provider123";
        Member member = MemberFixture.KAKAO.생성();
        Product product1 = ProductFixture.TEST_PRODUCT.생성(1L);
        Product product2 = ProductFixture.TEST_PRODUCT.생성(2L);

        List<Cart> carts = List.of(
                new Cart(1L, 2, true, member, product1, null, null),
                new Cart(2L, 1, true, member, product2, null, null)
        );

        when(memberRepository.findMemberByProviderId(providerId)).thenReturn(Optional.of(member));
        when(cartRepository.countByMemberId(member.getMemberId())).thenReturn(carts.size());

        CartItemCountResponse response = cartService.getCartItemCount(providerId);

        assertNotNull(response);
        assertEquals(carts.size(), response.getCount());
        verify(cartRepository).countByMemberId(member.getMemberId());
    }

    @Test
    @DisplayName("장바구니 아이템 선택 상태 업데이트")
    void updateCartSelectionTest() {
        Long cartId = 1L;
        boolean newSelectionStatus = false;
        Cart cart = new Cart(1L, 2, true, null, null, null, null);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        cartService.updateCartSelection(cartId, newSelectionStatus);

        verify(cartRepository).save(cart);
        assertFalse(cart.isSelected());
    }
}
