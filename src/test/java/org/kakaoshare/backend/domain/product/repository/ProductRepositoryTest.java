package org.kakaoshare.backend.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction;

@RepositoryTest
class ProductRepositoryTest {
    private static final int PAGE_SIZE = 20;
    public static final long CATEGORY_ID = 8L;
    public static final String PRICE = "PRICE";
    public static final String WISH_COUNT = "WISH_COUNT";
    
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    
    @ValueSource(strings = {PRICE, WISH_COUNT})
    @ParameterizedTest
    @DisplayName("상품 목록 조회는 가격과 위시를 기준으로 정렬되어 페이징 가능하다")
    void testProductPagination(String order) {
        PageRequest first = PageRequest.of(0, PAGE_SIZE,
                Direction.ASC, order);
        Page<SimpleProductDto> firstPage = productRepository.findAllByCategoryId(CATEGORY_ID, first);
        System.out.println(firstPage.getContent());
        
        Pageable next = first.next();
        Page<SimpleProductDto> nextPage = productRepository.findAllByCategoryId(CATEGORY_ID, next);
        System.out.println(nextPage.getContent());
        
        assertThat(firstPage.getSize()).isEqualTo(PAGE_SIZE);
        if (order.equals(PRICE)) {
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(SimpleProductDto::getPrice));
        } else if (order.equals(WISH_COUNT)) {
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(SimpleProductDto::getWishCount));
        }
        assertThat(nextPage.getSize()).isEqualTo(PAGE_SIZE);
    }
    
    @Test
    @DisplayName("정렬은 기본적으로 상품명을 기준으로 정렬된다")
    void testDefaultPagination() {
        PageRequest first = PageRequest.of(0, PAGE_SIZE);
        Page<SimpleProductDto> firstPage = productRepository.findAllByCategoryId(CATEGORY_ID, first);
        assertThat(firstPage.getContent().stream().map(SimpleProductDto::getName).toList())
                .isSortedAccordingTo(String::compareTo);
    }
    
    @Test
    @DisplayName("정렬 조건은 두가지 이상 가능하다")
    void testMultipleCondition() {
        Sort sort = Sort.by(Sort.Order.asc(SortUtil.PRICE.name()), Sort.Order.desc(SortUtil.PRODUCT_NAME.name()));
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE, sort);
        Page<SimpleProductDto> page = productRepository.findAllByCategoryId(CATEGORY_ID, pageRequest);
        page.forEach(System.out::println);
        
        assertThat(page.getContent().stream().map(SimpleProductDto::getPrice).toList())
                .isSortedAccordingTo(BigDecimal::compareTo);
        
        Map<BigDecimal, List<SimpleProductDto>> groupedByPrice = page.getContent().stream()
                .collect(Collectors.groupingBy(SimpleProductDto::getPrice));
        
        groupedByPrice.forEach((price, productsWithSamePrice) -> {
            if (productsWithSamePrice.size() > 1) {
                assertThat(productsWithSamePrice.stream().map(SimpleProductDto::getName).toList())
                        .isSortedAccordingTo(Comparator.reverseOrder());
            }
        });
    }
    
    @Test
    @DisplayName("자식 카테고리를 통해 조회하면 해당 카테고리 관련 상품만 나온다")
    void testFindProductsByChildCategoryId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);
        Page<SimpleProductDto> productDtos = productRepository.findAllByCategoryId(2L, pageRequest);
        // then
        assertThat(productDtos.getContent().size()).isEqualTo(PAGE_SIZE);
        
        assertThat(productDtos.map(SimpleProductDto::getBrandName)
                .stream().distinct().toList().size()).isEqualTo(PAGE_SIZE / 10 + (PAGE_SIZE % 10 == 0 ? 0 : 1));
    }
    
    @Test
    @DisplayName("부모 카테고리를 통해 조회하면 해당 카테고리 관련 상품만 나온다")
    void testFindProductsByParentCategoryId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 20000);
        Page<SimpleProductDto> productDtos = productRepository.findAllByCategoryId(1L, pageRequest);
        // then
        System.out.println(productDtos.getContent().size());
        assertThat(productDtos.getContent().size()).isEqualTo(250);
    }
}