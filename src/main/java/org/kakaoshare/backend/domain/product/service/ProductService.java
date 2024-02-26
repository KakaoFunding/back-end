package org.kakaoshare.backend.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.dto.DetailDescriptionResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.query.ProductRepositoryCustomImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepositoryCustomImpl productRepositoryCustomImpl;


    public DetailDescriptionResponse getProductDetail(Long productId) {
        DetailDescriptionResponse detailDescriptionResponse = productRepositoryCustomImpl.findProductWithDetailsAndPhotos(productId);
        if (detailDescriptionResponse == null) {
            throw new EntityNotFoundException("Product not found with id: " + productId);
        }
        return detailDescriptionResponse;
    }

}
