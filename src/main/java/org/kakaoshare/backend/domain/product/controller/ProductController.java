package org.kakaoshare.backend.domain.product.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.WishResponse;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.service.ProductService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    public static final int PAGE_DEFAULT_SIZE = 20;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long productId,
                                              @Nullable @LoggedInMember String providerId,
                                              @RequestParam(name = "tab", required = false, defaultValue = "description") String tab) {
        if ("description".equals(tab)) {
            DescriptionResponse response = productService.getProductDescription(productId, providerId);
            return ResponseEntity.ok(response);
        }
        if ("detail".equals(tab)) {
            DetailResponse response = productService.getProductDetail(productId, providerId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Invalid tab value");
    }

    @GetMapping
    public ResponseEntity<?> getSimpleProductsInPage(
            @Nullable @LoggedInMember String providerId,
            @RequestParam("categoryId") Long categoryId,
            @PageableDefault(size = PAGE_DEFAULT_SIZE) Pageable pageable) {
        PageResponse<?> simpleProductsPage = productService.getSimpleProductsPage(categoryId, pageable, providerId);
        return ResponseEntity.ok(simpleProductsPage);
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<?> getBrandsProducts(@PathVariable("brandId") Long brandId,
                                               @PageableDefault(size = PAGE_DEFAULT_SIZE) Pageable pageable) {
        PageResponse<?> simpleProductPage = productService.getSimpleProductsByBrandId(brandId, pageable);
        return ResponseEntity.ok(simpleProductPage);
    }


    @PostMapping("/{productId}/wishes")
    public ResponseEntity<?> resistWishingProduct(@LoggedInMember String providerId,
                                                  @PathVariable("productId") Long productId,
                                                  @RequestParam(name = "type") WishType type) {
        WishResponse response = productService.resisterProductInWishList(providerId, productId, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/{productId}/wishes")
    public ResponseEntity<?> cancelWisingProduct(@LoggedInMember String providerId,
                                                 @PathVariable("productId") Long productId) {
        WishResponse response = productService.removeWishlist(providerId, productId);
        return ResponseEntity.ok(response);
    }
}
