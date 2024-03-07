package org.kakaoshare.backend.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;


    public DescriptionResponse getProductDescription(Long productId) {
        DescriptionResponse descriptionResponse = productRepository.findProductWithDetailsAndPhotos(
                productId);
        if (descriptionResponse == null) {
            throw new EntityNotFoundException("Product not found with id: " + productId);
        }
        return descriptionResponse;
    }

    public DetailResponse getProductDetail(Long productId) {
        DetailResponse detailResponse = productRepository.findProductDetail(productId);
        if (detailResponse == null) {
            throw new EntityNotFoundException("Product not found with id: " + productId);
        }
        return detailResponse;
    }
    
    public Page<SimpleProductDto> getSimpleProductsPage(Long categoryId, Pageable pageable){
        Page<SimpleProductDto> productDtos = productRepository.findAllByCategoryId(categoryId, pageable);
        if (productDtos.isEmpty()){
            //TODO 2024 03 04 16:09:34 : 에러 구체화
            throw new IllegalArgumentException("빈 페이지 입니다.");
        }
        return productDtos;
    }
}
