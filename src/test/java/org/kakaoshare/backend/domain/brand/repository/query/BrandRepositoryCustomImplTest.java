package org.kakaoshare.backend.domain.brand.repository.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.brand.repository.BrandRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.BrandFixture.EDIYA;
import static org.kakaoshare.backend.fixture.BrandFixture.STARBUCKS;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;

@MockBean(JpaMetamodelMappingContext.class)
@RepositoryTest
class BrandRepositoryCustomImplTest {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품명으로 브랜드 조회")
    public void findBySearchConditions() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCake = CAKE.생성(1L, starbucks);

        final Brand ediya = EDIYA.생성();
        final Product ediyaCake = CAKE.생성(2L, ediya);

        productRepository.save(starbucksCake);
        productRepository.save(ediyaCake);

        final String keyword = "케이크";
        final Pageable pageable =  PageRequest.of(0, 4, Sort.unsorted());
        final List<SimpleBrandDto> actual = brandRepository.findBySearchConditions(keyword, pageable);
        assertThat(actual).contains(
                getSimpleBrandDto(starbucks),
                getSimpleBrandDto(ediya)
        );
    }

    @Test
    @DisplayName("상품명으로 브랜드 조회 최근 입점순")
    public void findBySearchConditionsOrderByCreatedAtDesc() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCake = CAKE.생성(1L, starbucks);

        final Brand ediya = EDIYA.생성();
        final Product ediyaCake = CAKE.생성(2L, ediya);

        productRepository.save(starbucksCake);
        productRepository.save(ediyaCake);

        final String keyword = "케이크";
        final Pageable pageable =  PageRequest.of(0, 4, Sort.by("createdAt").descending());
        final List<SimpleBrandDto> actual = brandRepository.findBySearchConditions(keyword, pageable);
        assertThat(actual).containsExactly(
                getSimpleBrandDto(ediya),
                getSimpleBrandDto(starbucks)
        );
    }

    private SimpleBrandDto getSimpleBrandDto(final Brand brand) {
        return new SimpleBrandDto(brand.getBrandId(), brand.getName(), brand.getIconPhoto());
    }
}