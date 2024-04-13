package org.kakaoshare.backend.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.error.SortErrorCode;
import org.kakaoshare.backend.common.util.sort.error.exception.NoMorePageException;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.dto.WishCancelEvent;
import org.kakaoshare.backend.domain.product.dto.WishResponse;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.error.ProductErrorCode;
import org.kakaoshare.backend.domain.product.error.exception.ProductException;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.wish.dto.WishReservationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    
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
    
    public Page<Product4DisplayDto> getSimpleProductsPage(Long categoryId, Pageable pageable) {
        Page<Product4DisplayDto> productDtos = productRepository.findAllByCategoryId(categoryId, pageable);
        if (productDtos.isEmpty()) {
            throw new NoMorePageException(SortErrorCode.NO_MORE_PAGE);
        }
        return productDtos;
    }
    
    public Page<ProductDto> getSimpleProductsByBrandId(Long brandId, Pageable pageable) {
        Page<ProductDto> productDtos = productRepository.findAllByBrandId(brandId, pageable);
        if (productDtos.isEmpty()) {
            throw new NoMorePageException(SortErrorCode.NO_MORE_PAGE);
        }
        return productDtos;
    }
    
    /**
     * 위시 추가시 위시 서비스에서 비동기적으로 위시 리스트에 등록
     * @see org.kakaoshare.backend.domain.wish.service.WishService
     */
    @Transactional
    public WishResponse resisterProductInWishList(final String providerId, final Long productId, final WishType type) {
        Product product = getProduct(productId);
        
        Integer wishCount = product.increaseWishCount();
        
        eventPublisher.publishEvent(WishReservationEvent.of(providerId,type,product));
        return WishResponse.of(product.getProductId(),wishCount);
    }
    
    /**
     * 위시 취소시 위시 서비스에서 비동기적으로 위시 리스트에서 제거
     * @see org.kakaoshare.backend.domain.wish.service.WishService
     */
    @Transactional
    public WishResponse removeWishlist(final String providerId, final Long productId) {
        Product product = getProduct(productId);
        
        Integer wishCount=product.decreaseWishCount();
        
        eventPublisher.publishEvent(WishCancelEvent.of(providerId,product));
        return WishResponse.of(product.getProductId(),wishCount);
    }
    
    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));
    }
}
