package org.kakaoshare.backend.domain.cart.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.cart.dto.CartClearResponse;
import org.kakaoshare.backend.domain.cart.dto.CartDeleteResponse;
import org.kakaoshare.backend.domain.cart.dto.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.dto.CartResponse;
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
import org.kakaoshare.backend.domain.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final OptionDetailRepository optionDetailRepository;

    @Transactional
    public CartRegisterResponse registerItem(Long productId, Long optionId, Long optionDetailId, String providerId) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(productId);
        Option option = getOptionById(optionId);
        OptionDetail optionDetail = getOptionDetailById(optionDetailId);

        Cart existingCart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElse(null);
        if (existingCart != null) {
            existingCart.updateItemCount(existingCart.getItemCount() + 1);
            cartRepository.save(existingCart);
        } else {
            Cart newCart = Cart.builder()
                    .member(member)
                    .product(product)
                    .option(option)
                    .optionDetail(optionDetail)
                    .itemCount(1)
                    .build();
            cartRepository.save(newCart);
        }

        return CartRegisterResponse.builder()
                .message("상품이 장바구니에 추가되었습니다.")
                .build();
    }

    @Transactional
    public CartRegisterResponse updateItem(Long productId, String providerId, int newQuantity) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(productId);

        Cart cart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("No cart item"));

        cart.updateItemCount(newQuantity - cart.getItemCount());
        cartRepository.save(cart);

        return CartRegisterResponse.builder()
                .message("장바구니 상품의 수량이 업데이트되었습니다.")
                .build();
    }

    @Transactional
    public CartDeleteResponse deleteItem(Long productId, String providerId) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(productId);

        Cart cart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("No cart item"));

        cartRepository.delete(cart);
        return CartDeleteResponse.builder()
                .cartId(cart.getCartId())
                .message("장바구니 상품이 삭제되었습니다.")
                .build();
    }

    public List<CartResponse> getCartItems(String providerId) {
        Member member = findMemberByProviderId(providerId);
        List<Cart> carts = cartRepository.findByMemberId(member.getMemberId());
        return carts.stream()
                .map(CartResponse::from)
                .toList();
    }

    @Transactional
    public CartClearResponse clearCartItems(String providerId) {
        Member member = findMemberByProviderId(providerId);
        cartRepository.deleteByMemberId(member.getMemberId());
        return CartClearResponse.builder()
                .success(true)
                .message("장바구니의 모든 상품이 삭제 되었습니다.")
                .build();
    }

    private Member findMemberByProviderId(String providerId) {
        return memberRepository.findMemberByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
    }

    private Product findProductByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
    }

    private Option findOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));
    }

    private OptionDetail findOptionDetailById(Long optionDetailId) {
        return optionDetailRepository.findById(optionDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option detail ID"));
    }

    private Option getOptionById(Long optionId) {
        return optionId != null ? optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID")) : null;
    }

    private OptionDetail getOptionDetailById(Long optionDetailId) {
        return optionDetailId != null ? optionDetailRepository.findById(optionDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option detail ID")) : null;
    }

}
