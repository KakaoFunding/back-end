package org.kakaoshare.backend.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductQueryRepository;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;


    public DetailResponse getProductDetail(Long productId) {
        Product product = productQueryRepository.findProductWithDetailsAndPhotos(productId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with id: " + productId);
        }
        return DetailResponse.from(product);
    }
}
