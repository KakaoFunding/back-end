package org.kakaoshare.backend.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ProductRepositoryTest {
    private static final int PAGE_SIZE = 20;
    public static final long CATEGORY_ID = 8L;
    
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @ValueSource(strings = {"price","wish"})
    @ParameterizedTest
    @DisplayName("상품 목록 조회는 가격과 위시를 기준으로 정렬되어 페이징 가능하다")
    void testProductPagination(String order) {
        
        Pageable first=PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Order.by(order)));
        Page<SimpleProductDto> firstPage = productRepository.findAllByCategoryId(CATEGORY_ID, first);
        System.out.println(firstPage.getContent());
        
        Pageable next = first.next();
        Page<SimpleProductDto> nextPage = productRepository.findAllByCategoryId(CATEGORY_ID, next);
        System.out.println(nextPage.getContent());
        
        assertThat(firstPage.getSize()).isEqualTo(PAGE_SIZE);
        if(order.equals("price")){
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(SimpleProductDto::getPrice));
        }else if (order.equals("wish")){
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
}