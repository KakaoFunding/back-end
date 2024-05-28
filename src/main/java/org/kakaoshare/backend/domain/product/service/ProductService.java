package org.kakaoshare.backend.domain.product.service;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
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
import org.kakaoshare.backend.domain.product.dto.WishCancelEvent;
import org.kakaoshare.backend.domain.product.dto.WishResponse;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.exception.ProductErrorCode;
import org.kakaoshare.backend.domain.product.exception.ProductException;
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
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;


    public DescriptionResponse getProductDescription(Long productId, @Nullable String providerId) {
        Product product = findProductById(productId);

        if (providerId != null) {
            Member member = findMemberById(providerId);
            return productRepository.findProductWithDetailsAndPhotosWithMember(product, member);
        } else {
            return productRepository.findProductWithDetailsAndPhotosWithoutMember(product);
        }
    }

    public DetailResponse getProductDetail(Long productId, @Nullable String providerId) {
        Product product = findProductById(productId);

        if (product == null) {
            throw new BusinessException(GlobalErrorCode.RESOURCE_NOT_FOUND);
        }

        if (providerId != null) {
            Member member = findMemberById(providerId);
            return productRepository.findProductDetailWithMember(product, member);
        }
        return productRepository.findProductDetailWithoutMember(product);
    }

    public PageResponse<?> getSimpleProductsPage(Long categoryId, Pageable pageable, final String providerId) {
        Page<Product4DisplayDto> productDtos = productRepository.findAllByCategoryId(categoryId, pageable, providerId);
        if (productDtos.isEmpty()) {
            throw new NoMorePageException(SortErrorCode.NO_MORE_PAGE);
        }
        return PageResponse.from(productDtos);
    }

    public PageResponse<?> getSimpleProductsByBrandId(Long brandId, Pageable pageable) {
        Page<ProductDto> productDtos = productRepository.findAllByBrandId(brandId, pageable);
        if (productDtos.isEmpty()) {
            throw new NoMorePageException(SortErrorCode.NO_MORE_PAGE);
        }
        return PageResponse.from(productDtos);
    }

    /**
     * 위시 추가시 위시 서비스에서 비동기적으로 위시 리스트에 등록
     *
     * @see org.kakaoshare.backend.domain.wish.service.WishService
     */
    @Transactional
    public WishResponse resisterProductInWishList(final String providerId, final Long productId, final WishType type) {
        Product product = findProductById(productId);

        product.increaseWishCount();

        eventPublisher.publishEvent(WishReservationEvent.of(providerId, type, product));
        return WishResponse.from(product);
    }

    /**
     * 위시 취소시 위시 서비스에서 비동기적으로 위시 리스트에서 제거
     *
     * @see org.kakaoshare.backend.domain.wish.service.WishService
     */
    @Transactional
    public WishResponse removeWishlist(final String providerId, final Long productId) {
        Product product = findProductById(productId);

        product.decreaseWishCount();

        eventPublisher.publishEvent(WishCancelEvent.of(providerId, product));
        return WishResponse.from(product);
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));
    }

    private Member findMemberById(final String providerId) {
        return memberRepository.findMemberByProviderId(providerId).orElseThrow(() -> new MemberException(
                MemberErrorCode.NOT_FOUND));
    }
}
