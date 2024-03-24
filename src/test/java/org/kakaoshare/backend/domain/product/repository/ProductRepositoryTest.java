package org.kakaoshare.backend.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.common.util.sort.SortUtil.*;

@RepositoryTest
class ProductRepositoryTest {
    private static final int PAGE_SIZE = 20;
    public static final long CATEGORY_ID = 8L;
    
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    @ValueSource(strings = {"PRICE", "WISH_COUNT"})
    @ParameterizedTest
    @DisplayName("상품 목록 조회는 가격과 위시를 기준으로 정렬되어 페이징 가능하다")
    void testProductPagination(String order) {
        PageRequest first = PageRequest.of(0, PAGE_SIZE,
                Sort.Direction.ASC, order);
        Page<Product4DisplayDto> firstPage = productRepository.findAllByCategoryId(CATEGORY_ID, first);
        System.out.println(firstPage.getContent());

        Pageable next = first.next();
        Page<Product4DisplayDto> nextPage = productRepository.findAllByCategoryId(CATEGORY_ID, next);
        System.out.println(nextPage.getContent());

        assertThat(firstPage.getSize()).isEqualTo(PAGE_SIZE);
        assertThat(nextPage.getSize()).isEqualTo(PAGE_SIZE);
        if (order.equals(PRICE)) {
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(Product4DisplayDto::getPrice));
        } else if (order.equals(WISH_COUNT)) {
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(Product4DisplayDto::getWishCount));
        }
        assertThat(nextPage.getSize()).isEqualTo(PAGE_SIZE);
    }
    
    @Test
    @DisplayName("정렬은 기본적으로 상품명을 기준으로 정렬된다")
    void testDefaultPagination() {
        PageRequest first = PageRequest.of(0, PAGE_SIZE);
        Page<Product4DisplayDto> firstPage = productRepository.findAllByCategoryId(CATEGORY_ID, first);
        assertThat(firstPage.getContent().stream().map(Product4DisplayDto::getName).toList())
                .isSortedAccordingTo(String::compareTo);
    }
    
    @Test
    @DisplayName("정렬 조건은 두가지 이상 가능하다")
    void testMultipleCondition() {
        Sort sort = Sort.by(Sort.Order.asc(PRICE.name()), Sort.Order.desc(PRODUCT_NAME.name()));
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE, sort);
        Page<Product4DisplayDto> page = productRepository.findAllByCategoryId(CATEGORY_ID, pageRequest);
        page.forEach(System.out::println);
        
        assertThat(page.getContent().stream().map(Product4DisplayDto::getPrice).toList())
                .isSortedAccordingTo(Long::compareTo);
        
        Map<Long, List<Product4DisplayDto>> groupedByPrice = page.getContent().stream()
                .collect(Collectors.groupingBy(Product4DisplayDto::getPrice));
        
        groupedByPrice.forEach((price, productsWithSamePrice) -> {
            if (productsWithSamePrice.size() > 1) {
                assertThat(productsWithSamePrice.stream().map(Product4DisplayDto::getName).toList())
                        .isSortedAccordingTo(Comparator.reverseOrder());
            }
        });
    }
    
    @Test
    @DisplayName("자식 카테고리를 통해 조회하면 해당 카테고리 관련 상품만 나온다")
    void testFindProductsByChildCategoryId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);
        Page<Product4DisplayDto> productDtos = productRepository.findAllByCategoryId(6L, pageRequest);
        // then
        assertThat(productDtos.getContent().size()).isEqualTo(PAGE_SIZE);
        assertThat(productDtos.getTotalElements()).isEqualTo(400);
        
    }
    
    @Test
    @DisplayName("부모 카테고리를 통해 조회하면 해당 카테고리 관련 상품만 나온다")
    void testFindProductsByParentCategoryId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);
        Page<Product4DisplayDto> productDtos = productRepository.findAllByCategoryId(1L, pageRequest);
        // then
        assertThat(productDtos.getContent().size()).isEqualTo(PAGE_SIZE);
        assertThat(productDtos.getTotalElements()).isEqualTo(2000);
    }
    
    @ValueSource(longs = {1L,6L})
    @ParameterizedTest
    @DisplayName("카테고리 상품 목록은 위시수로 정렬 가능하다")
    void testSortByWishCount(Long categoryId) {
        // given
        PageRequest pageRequest = PageRequest.of(0, 20000, Sort.by(WISH_COUNT.name()));
        // when
        Page<Product4DisplayDto> firstPage = productRepository.findAllByCategoryId(categoryId, pageRequest);
        // then
        assertThat(firstPage.getContent()).isSortedAccordingTo(Comparator.comparing(Product4DisplayDto::getWishCount)
        );
    }
    
    @Test
    @DisplayName("브랜드에 등록된 상품들을 조회 가능하다")
    void testFindAllByBrandId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<ProductDto> productDtos = productRepository.findAllByBrandId(1L, pageRequest);
        // then
        assertThat(productDtos.getContent().size()).isEqualTo(20);
        assertThat(productDtos.getTotalElements()).isEqualTo(200);
    }
    
    @Test
    @DisplayName("브랜드 상품 목록은 상품 등록 시간으로 정렬 가능하다")
    void testSortByCreateAt() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 20000, Sort.by(MOST_RECENT.name()));
        // when
        Page<ProductDto> productPage = productRepository.findAllByBrandId(1L, pageRequest);
        
        assertThat(productPage.getContent()).isSortedAccordingTo((o1, o2) -> {
                    Product o1Entity = productRepository.findById(o1.getProductId()).orElseThrow();
                    Product o2Entity = productRepository.findById(o2.getProductId()).orElseThrow();
                    return o1Entity.getCreatedAt().compareTo(o2Entity.getCreatedAt());
                }
        );
    }
}