package org.kakaoshare.backend.domain.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.util.SortUtil;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.kakaoshare.backend.domain.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        if ("detail".equals(tab)) {
            DetailResponse response = productService.getProductDetail(productId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Invalid tab value");
    }
    
    @GetMapping
    public ResponseEntity<?> getSimpleProductsInfo(@RequestParam("categoryId") Long categoryId,
                                                   @PageableDefault(value = 20) Pageable pageable){
        try {
            //TODO 2024 03 04 20:19:18 : AOP로 분리 고려
            SortUtil.checkSortCondition(pageable);
            Page<SimpleProductDto> simpleProductsPage = productService.getSimpleProductsPage(categoryId, pageable);
            return ResponseEntity.ok(simpleProductsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
