package org.kakaoshare.backend.domain.cart.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.cart.dto.CartIdResponse;
import org.kakaoshare.backend.domain.cart.dto.delete.CartClearResponse;
import org.kakaoshare.backend.domain.cart.dto.delete.CartDeleteResponse;
import org.kakaoshare.backend.domain.cart.dto.inquiry.CartItemCountResponse;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterRequest;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.dto.inquiry.CartResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.cart.exception.CartErrorCode;
import org.kakaoshare.backend.domain.cart.exception.CartException;
import org.kakaoshare.backend.domain.cart.repository.CartRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.entity.OptionDetail;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.option.repository.OptionRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
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
    public CartRegisterResponse registerItem(CartRegisterRequest request, String providerId) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(request.getProductId());
        Option option = resolveOption(request.getOptionId(), product);
        OptionDetail optionDetail = resolveOptionDetail(request.getOptionDetailId(), option);

        Cart existingCart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElse(null);
        if (existingCart != null) {
            existingCart.updateItemCount(existingCart.getItemCount() + 1);
            cartRepository.save(existingCart);
            return CartIdResponse.from(existingCart, CartRegisterResponse.class);
        } else {
            Cart newCart = Cart.builder()
                    .member(member)
                    .product(product)
                    .option(option)
                    .optionDetail(optionDetail)
                    .itemCount(request.getItemCount())
                    .isSelected(true)
                    .build();
            cartRepository.save(newCart);
            return CartIdResponse.from(newCart, CartRegisterResponse.class);
        }
    }

    @Transactional
    public CartRegisterResponse updateItem(Long productId, String providerId, int newQuantity) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(productId);

        Cart cart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("No cart item"));

        cart.updateItemCount(newQuantity - cart.getItemCount());
        cartRepository.save(cart);

        return CartIdResponse.from(cart, CartRegisterResponse.class);
    }

    @Transactional
    public CartDeleteResponse deleteItem(Long productId, String providerId) {
        Member member = findMemberByProviderId(providerId);
        Product product = findProductByProductId(productId);

        Cart cart = cartRepository.findByMemberIdAndProductId(member.getMemberId(), product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("No cart item"));

        cartRepository.delete(cart);
        return CartIdResponse.from(cart, CartDeleteResponse.class);
    }

    public List<CartResponse> getCartItems(String providerId) {
        Member member = findMemberByProviderId(providerId);
        List<Cart> carts = cartRepository.findByMemberId(member.getMemberId());
        return carts.stream()
                .map(CartResponse::from)
                .toList();
    }

    public CartItemCountResponse getCartItemCount(String providerId) {
        Member member = findMemberByProviderId(providerId);
        int count = cartRepository.countByMemberId(member.getMemberId());
        return CartItemCountResponse.from(count);
    }

    @Transactional
    public CartRegisterResponse updateCartSelection(Long cartId, boolean isSelected) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));
        cart.changeIsSelected(isSelected);
        cartRepository.save(cart);

        return CartIdResponse.from(cart, CartRegisterResponse.class);
    }

    @Transactional
    public CartClearResponse clearCartItems(String providerId) {
        Member member = findMemberByProviderId(providerId);
        cartRepository.deleteByMemberId(member.getMemberId());
        return CartClearResponse.from();
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
        if (optionId != null) {
            return findOptionById(optionId);
        } else {
            return null;
        }
    }

    private OptionDetail getOptionDetailById(Long optionDetailId) {
        if (optionDetailId != null) {
            return findOptionDetailById(optionDetailId);
        } else {
            return null;
        }
    }

    private Option resolveOption(Long optionId, Product product) {
        if (optionId == null) {
            List<Option> options = optionRepository.findByProductId(product.getProductId());
            return options.isEmpty() ? null : options.get(0);
        }
        return getOptionById(optionId);
    }

    private OptionDetail resolveOptionDetail(Long optionDetailId, Option option) {
        if (optionDetailId == null) {
            if (option != null) {
                List<OptionDetail> details = optionDetailRepository.findByOptionId(option.getOptionsId());
                return details.isEmpty() ? null : details.get(0);
            }
            return null;
        }
        return getOptionDetailById(optionDetailId);
    }

}
