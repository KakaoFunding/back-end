package org.kakaoshare.backend.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.cart.dto.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.cart.repository.CartRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
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

    @Transactional
    public CartRegisterResponse registerItem(Long productId, String providerId) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(productId);

        Cart existingCart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElse(null);
        if (existingCart != null) {
            existingCart.updateItemCount(existingCart.getItemCount() + 1);
            cartRepository.save(existingCart);
        } else {
            Cart newCart = Cart.builder()
                    .member(member)
                    .product(product)
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

    private Member findMemberByProviderId(String providerId) {
        return memberRepository.findMemberByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
    }

    private Product findProductByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
    }
}
