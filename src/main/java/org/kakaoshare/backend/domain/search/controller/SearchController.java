package org.kakaoshare.backend.domain.search.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.search.dto.BrandSearchRequest;
import org.kakaoshare.backend.domain.search.dto.ProductSearchRequest;
import org.kakaoshare.backend.domain.search.service.SearchService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private static final int BRAND_DEFAULT_SIZE = 4;
    private static final int PRODUCT_DEFAULT_SIZE = 20;
    private static final int PRODUCT_GROUP_BY_BRAND_DEFAULT_SIZE = 9;

    private final SearchService searchService;

    @GetMapping("/products")
    public ResponseEntity<?> searchProducts(@LoggedInMember @Nullable final String providerId,
                                            @ModelAttribute final ProductSearchRequest productSearchRequest,
                                            @PageableDefault(size = PRODUCT_DEFAULT_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(
                searchService.searchProducts(productSearchRequest, pageable, providerId)
        );
    }

    @GetMapping("/brands")
    public ResponseEntity<?> searchBrands(@ModelAttribute final BrandSearchRequest brandSearchRequest,
                                          @PageableDefault(size = BRAND_DEFAULT_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(
                searchService.searchBrands(brandSearchRequest, pageable)
        );
    }

    @GetMapping("/products/all")
    public ResponseEntity<?> searchAll(@LoggedInMember @Nullable final String providerId,
                                       @ModelAttribute final BrandSearchRequest brandSearchRequest,
                                       @PageableDefault(size = BRAND_DEFAULT_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(
                searchService.searchProductGroupByBrand(brandSearchRequest, pageable, providerId)
        );
    }
}
