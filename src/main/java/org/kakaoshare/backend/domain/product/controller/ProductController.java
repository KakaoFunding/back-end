package org.kakaoshare.backend.domain.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long productId,
                                                                @RequestParam(name = "tab", required = false, defaultValue = "description") String tab) {
        if ("description".equals(tab)) {
            DescriptionResponse response = productService.getProductDescription(productId);
            return ResponseEntity.ok(response);
        }
        if ("detail".equals(tab)){
            DetailResponse response = productService.getProductDetail(productId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Invalid tab value");
    }
}
