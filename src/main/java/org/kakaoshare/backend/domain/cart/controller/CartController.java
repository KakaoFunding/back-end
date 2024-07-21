package org.kakaoshare.backend.domain.cart.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.cart.dto.delete.CartClearResponse;
import org.kakaoshare.backend.domain.cart.dto.delete.CartDeleteResponse;
import org.kakaoshare.backend.domain.cart.dto.inquiry.CartItemCountResponse;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterRequest;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.dto.inquiry.CartResponse;
import org.kakaoshare.backend.domain.cart.service.CartService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping()
    public ResponseEntity<?> registerItem(@RequestBody CartRegisterRequest request, @LoggedInMember String providerId) {
        CartRegisterResponse response = cartService.registerItem(request, providerId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CartRegisterResponse> updateCartItem(@PathVariable Long productId,
                                                               @LoggedInMember String providerId,
                                                               @RequestParam("quantity") int quantity) {
        CartRegisterResponse response = cartService.updateItem(productId, providerId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CartDeleteResponse> deleteCartItem(@PathVariable Long productId,
                                                             @LoggedInMember String providerId) {
        CartDeleteResponse response = cartService.deleteItem(productId, providerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getCartItems(@LoggedInMember String providerId) {
        List<CartResponse> cartItems = cartService.getCartItems(providerId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/itemCount")
    public ResponseEntity<?> getCartItemCount(@LoggedInMember String providerId){
        CartItemCountResponse response = cartService.getCartItemCount(providerId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<CartClearResponse> clearCart(@LoggedInMember String providerId) {
        CartClearResponse response = cartService.clearCartItems(providerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cartId}/selection")
    public ResponseEntity<?> updateCartSelection(@PathVariable Long cartId, @RequestParam boolean isSelected) {
        CartRegisterResponse response = cartService.updateCartSelection(cartId, isSelected);
        return ResponseEntity.ok(response);
    }

}
