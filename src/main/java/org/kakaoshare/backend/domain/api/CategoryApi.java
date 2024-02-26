package org.kakaoshare.backend.domain.api;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryApi {
    private final CategoryService categoryService;
    
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(){
        List<CategoryDto> categories = categoryService.getParentCategory();
        return ResponseEntity.ok(categories);
    }
}
