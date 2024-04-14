package org.kakaoshare.backend.domain.product.repository.query;

import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.search.dto.SimpleBrandProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {
    Page<Product4DisplayDto> findAllByCategoryId(Long categoryId, Pageable pageable);
    Page<ProductDto> findAllByBrandId(final Long brandId, final Pageable pageable);
    Page<ProductDto> findAllByIds(final List<Long> ids, final Pageable pageable);
    DescriptionResponse findProductWithDetailsAndPhotos(Long productId);
    DetailResponse findProductDetail(Long productId);
    Page<Product4DisplayDto> findBySearchConditions(final String keyword, final Integer minPrice, final Integer maxPrice, final List<String> categories, final Pageable pageable);
    Page<SimpleBrandProductDto> findBySearchConditionsGroupByBrand(final String keyword, final Pageable pageable);
    Map<Long, Long> findAllPriceByIdsGroupById(final List<Long> productIds);
    Map<Long, String> findAllNameByIdsGroupById(final List<Long> productIds);
}
