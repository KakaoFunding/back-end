package org.kakaoshare.backend.domain.brand.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    
    @GetMapping
    public ResponseEntity<?> getSimpleBrandsInfo(@RequestParam("categoryId") final Long categoryId) {
        final List<SimpleBrandDto> simpleBrandDtos = brandService.getSimpleBrandPage(categoryId);
        return ResponseEntity.ok(simpleBrandDtos);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<?> getBrandNameWithIcon(@PathVariable Long brandId){
        SimpleBrandDto brandDto = brandService.getBrandNameWithIcon(brandId);
        return ResponseEntity.ok(brandDto);
    }
}
