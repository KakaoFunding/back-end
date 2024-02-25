package org.kakaoshare.backend.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.CustomDataJpaTest;
import org.kakaoshare.backend.domain.product.entity.query.SimpleProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataJpaTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @ValueSource(strings = {"price","wish"})
    @ParameterizedTest
    @DisplayName("상품 목록 조회는 가격과 위시를 기준으로 정렬되어 페이징 가능하다")
    void testProductPagination(String order) {
        
        Pageable first=PageRequest.of(0,5, Sort.by(Sort.Order.by(order)));
        Page<SimpleProductDto> firstPage = productRepository.findAllByCategoryId(8L, first);
        System.out.println(firstPage.getContent());
        
        Pageable next = first.next();
        Page<SimpleProductDto> nextPage = productRepository.findAllByCategoryId(8L, next);
        System.out.println(nextPage.getContent());
        
        assertThat(firstPage.getSize()).isEqualTo(5);
        if(order.equals("price")){
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(SimpleProductDto::getPrice));
        }else if (order.equals("wish")){
            assertThat(firstPage.getContent())
                    .isSortedAccordingTo(Comparator.comparing(SimpleProductDto::getWishCount));
        }
        assertThat(nextPage.getSize()).isEqualTo(5);
    }
    
    @Test
    void testDefaultPagination() {
        PageRequest first = PageRequest.of(0, 5);
        Page<SimpleProductDto> firstPage = productRepository.findAllByCategoryId(8L, first);
        assertThat(firstPage.getContent().stream().map(SimpleProductDto::getName).toList())
                .isSortedAccordingTo(String::compareTo);
    }
}