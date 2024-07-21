package org.kakaoshare.backend.domain.category.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.dto.CategoryHeaderResponse;
import org.kakaoshare.backend.domain.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryDto> categories = categoryService.getParentCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        try {
            CategoryDto categoryDto = categoryService.getParentCategory(categoryId);
            return ResponseEntity.ok(categoryDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<?> getSubCategory(@PathVariable(name = "categoryId") Long categoryId,
                                            @PathVariable(name = "subcategoryId") Long subcategoryId) {
        try {
            CategoryDto categoryDto = categoryService.getChildCategory(categoryId, subcategoryId);
            return ResponseEntity.ok(categoryDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/{categoryId}/header")
    public ResponseEntity<?> getCategoryHeader(@PathVariable Long categoryId) {
        CategoryHeaderResponse headerResponse = categoryService.getHeaderResponse(categoryId);
        return ResponseEntity.ok(headerResponse);
    }
}
