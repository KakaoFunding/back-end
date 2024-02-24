package org.kakaoshare.backend.domain.brand.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.CustomDataJpaTest;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

@CustomDataJpaTest
class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    JPAQueryFactory queryFactory;
    
    
    @Test
    @Transactional(readOnly = true)
    void testDsl() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("spring data jpa");
        List<Brand> byCategoryCategoryId = brandRepository.findByCategory_CategoryId(1L);
        stopWatch.stop();

        stopWatch.start("querydsl fetch join");
        List<Brand> brands = brandRepository.findAllByCategoryId(1L);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        System.out.println(brands.size());
        System.out.println(byCategoryCategoryId.size());
    }
    
}