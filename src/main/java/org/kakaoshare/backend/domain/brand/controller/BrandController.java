package org.kakaoshare.backend.domain.brand.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    
    @GetMapping
    public ResponseEntity<?> getSimpleBrandsInfo(@RequestParam("categoryId") final Long categoryId,
                                                 @PageableDefault(size = 100) final Pageable pageable) {
        Page<SimpleBrandDto> simpleBrandPage = brandService.getSimpleBrandPage(categoryId, pageable);
        return ResponseEntity.ok(simpleBrandPage);
    }
}
