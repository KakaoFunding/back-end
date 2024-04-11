package org.kakaoshare.backend.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.error.SortErrorCode;
import org.kakaoshare.backend.common.util.sort.error.exception.NoMorePageException;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.error.ProductErrorCode;
import org.kakaoshare.backend.domain.product.error.exception.ProductException;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.wish.dto.WishEvent;
import org.kakaoshare.backend.domain.wish.entity.Wish;
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
    private final MemberRepository memberRepository;
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
    
    @Transactional
    public Integer resistProductInWishList(final String providerId, final Long productId, final WishType type) {
        Member member = getMember(providerId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));
        
        Integer wishCount = product.increaseWish();
        
        Wish wish = Wish.builder()
                .member(member)
                .product(product)
                .isPublic(WishType.OTHERS.equals(type))
                .build();
        
        eventPublisher.publishEvent(new WishEvent(wish));
        return wishCount;
    }
    
    private Member getMember(final String providerId) {
        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return member;
    }
}
