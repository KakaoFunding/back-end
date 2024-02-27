package org.kakaoshare.backend.domain.brand.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.CustomDataJpaTest;
import org.kakaoshare.backend.domain.brand.entity.query.SimpleBrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataJpaTest
@Transactional(readOnly = true)
class BrandRepositoryTest {
    public static final long PARENT_ID = 1L;
    public static final long CHILD_ID = 6L;
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    JPAQueryFactory queryFactory;
    
    
    @Test
    void findAllSimpleBrandByChildCategoryId() {
        List<SimpleBrandDto> brands = brandRepository.findAllSimpleBrandByChildId(CHILD_ID);
        assertThat(brands.size()).isEqualTo(5);
    }
    
    
    @Test
    void findAllSimpleBrandByParentCategoryId() {
        List<SimpleBrandDto> brands = brandRepository.findAllSimpleBrandByParentId(PARENT_ID);
        assertThat(brands.size()).isEqualTo(25);
    }
}