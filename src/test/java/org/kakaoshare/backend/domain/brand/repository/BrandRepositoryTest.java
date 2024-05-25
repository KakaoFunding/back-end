package org.kakaoshare.backend.domain.brand.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @DisplayName("자식 카테고리 id를 통해 브랜드 목록 조회")
    void findAllSimpleBrandByChildCategoryId() {
        List<SimpleBrandDto> simpleBrandDtosByChild = brandRepository.findAllSimpleBrandByCategoryId(CHILD_ID);
        assertThat(simpleBrandDtosByChild.size()).isEqualTo(1);
        assertThat(simpleBrandDtosByChild)
                .isSortedAccordingTo((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
    }
    
    @Test
    @DisplayName("부모 카테고리 id를 통해 자식 카테고리들이 가진 브랜드 목록 조회")
    void findAllSimpleBrandByParentCategoryId() {
        List<SimpleBrandDto> simpleBrandDtosByParent = brandRepository.findAllSimpleBrandByCategoryId(PARENT_ID);
        assertThat(simpleBrandDtosByParent.size()).isEqualTo(50);
        assertThat(simpleBrandDtosByParent)
                .isSortedAccordingTo((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
    }
}