package org.kakaoshare.backend.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.repository.BrandRepository;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.search.dto.BrandSearchRequest;
import org.kakaoshare.backend.domain.search.dto.ProductSearchRequest;
import org.kakaoshare.backend.domain.search.dto.SimpleBrandProductDto;
import org.kakaoshare.backend.domain.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

// TODO: 3/19/24 카테고리 필터링, 위시 여부는 아직 구현 X
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SearchService {
    private final BrandRepository brandRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public PageResponse<?> searchProducts(final ProductSearchRequest productSearchRequest,
                                          final Pageable pageable,
                                          final String providerId) {
        final Page<Product4DisplayDto> page = findProductsBySearchConditions(productSearchRequest, pageable,providerId);
        return PageResponse.from(page);
    }

    public List<SimpleBrandDto> searchBrands(final BrandSearchRequest brandSearchRequest,
                                             final Pageable pageable) {
        final String keyword = brandSearchRequest.keyword();
        return brandRepository.findBySearchConditions(keyword, pageable);
    }

    public PageResponse<?> searchProductGroupByBrand(final BrandSearchRequest brandSearchRequest,
                                                     final Pageable pageable,
                                                     final String providerId) {
        final String keyword = brandSearchRequest.keyword();
        final Page<SimpleBrandProductDto> slice = productRepository.findBySearchConditionsGroupByBrand(keyword, pageable,providerId);
        return PageResponse.from(slice);
    }

    private Page<Product4DisplayDto> findProductsBySearchConditions(final ProductSearchRequest productSearchRequest,
                                                                     final Pageable pageable,
                                                                    final String providerId) {
        final String keyword = productSearchRequest.keyword();
        final List<String> categories = productSearchRequest.categories();
        final Integer minPrice = productSearchRequest.minPrice();
        final Integer maxPrice = productSearchRequest.maxPrice();
        return productRepository.findBySearchConditions(keyword, minPrice, maxPrice, categories, pageable,providerId);
    }

    private boolean isLoggedIn(final String providerId) {
        return StringUtils.hasText(providerId);
    }
}
