package org.kakaoshare.backend.domain.cart.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.cart.dto.CartClearResponse;
import org.kakaoshare.backend.domain.cart.dto.CartDeleteResponse;
import org.kakaoshare.backend.domain.cart.dto.CartRegisterRequest;
import org.kakaoshare.backend.domain.cart.dto.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.dto.CartResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.cart.service.CartService;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PutMapping("/update/{productId}")
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

    @DeleteMapping("/clear")
    public ResponseEntity<CartClearResponse> clearCart(@LoggedInMember String providerId) {
        CartClearResponse response = cartService.clearCartItems(providerId);
        return ResponseEntity.ok(response);
    }

}
