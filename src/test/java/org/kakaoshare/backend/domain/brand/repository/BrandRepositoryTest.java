package org.kakaoshare.backend.domain.brand.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
@Transactional(readOnly = true)
class BrandRepositoryTest {
    public static final long PARENT_ID = 1L;
    public static final long CHILD_ID = 6L;
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    JPAQueryFactory queryFactory;
    
    
    @Test
    @DisplayName("자식 카테고리 id를 통해 브랜드 목록을 페이징 조회 가능하다")
    void findAllSimpleBrandByChildCategoryId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 3);
        PageRequest next = pageRequest.next();
        
        // when
        Page<SimpleBrandDto> firstPage = brandRepository.findAllSimpleBrandByCategoryId(CHILD_ID, pageRequest);
        Page<SimpleBrandDto> nextPage = brandRepository.findAllSimpleBrandByCategoryId(CHILD_ID, next);
        // then
        assertThat(firstPage.getContent().size()).isEqualTo(3);
        assertThat(nextPage.getContent().size()).isEqualTo(2);
        
        assertThat(firstPage.getContent())
                .isSortedAccordingTo((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));
        assertThat(nextPage.getContent())
                .isSortedAccordingTo((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));
    }
    
    @Test
    @DisplayName("부모 카테고리 id를 통해 자식 카테고리들이 가진 브랜드 목록을 페이징 조회 가능하다")
    void findAllSimpleBrandByParentCategoryId() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageRequest next = pageRequest.next();
        
        // when
        Page<SimpleBrandDto> firstPage = brandRepository.findAllSimpleBrandByCategoryId(PARENT_ID, pageRequest);
        Page<SimpleBrandDto> nextPage = brandRepository.findAllSimpleBrandByCategoryId(PARENT_ID, next);
        // then
        assertThat(firstPage.getContent().size()).isEqualTo(10);
        assertThat(nextPage.getContent().size()).isEqualTo(10);
        
        assertThat(firstPage.getContent())
                .isSortedAccordingTo((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));
        assertThat(nextPage.getContent())
                .isSortedAccordingTo((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));
    }
}