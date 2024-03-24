package org.kakaoshare.backend.domain.product.repository.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.search.dto.SimpleBrandProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.BrandFixture.EDIYA;
import static org.kakaoshare.backend.fixture.BrandFixture.STARBUCKS;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.kakaoshare.backend.fixture.ProductFixture.COFFEE;


@RepositoryTest
class ProductRepositoryCustomImplTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품명으로 상품 조회")
    public void findBySearchConditions() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCake = CAKE.브랜드_설정_생성(starbucks);
        final Product starbucksCoffee = COFFEE.브랜드_설정_생성(starbucks);

        final Brand ediya = EDIYA.생성();
        final Product ediyaCake = CAKE.브랜드_설정_생성(ediya);
        final Product ediyaCoffee = COFFEE.브랜드_설정_생성(ediya);

        productRepository.save(starbucksCake);
        productRepository.save(starbucksCoffee);
        productRepository.save(ediyaCake);
        productRepository.save(ediyaCoffee);

        final String keyword = "커피";
        final Pageable pageable = PageRequest.of(0, 4, Sort.unsorted());
        final Slice<Product4DisplayDto> slice = productRepository.findBySearchConditions(keyword, null, null, null, pageable);

        final List<Product4DisplayDto> products = slice.getContent();
        assertThat(products.size()).isEqualTo(2);
        assertThat(products).usingRecursiveComparison().comparingOnlyFields("name").isEqualTo(
                List.of(getProduct4DisplayDto(ediyaCoffee), getProduct4DisplayDto(starbucksCoffee))
        );
    }

    @Test
    @DisplayName("상품명으로 상품 조회 낮은 가격 순")
    public void findBySearchConditionsOrderByPriceAsc() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCoffee1 = COFFEE.브랜드_가격_설정_생성(starbucks, BigDecimal.valueOf(7_000));
        final Product starbucksCoffee2 = COFFEE.브랜드_가격_설정_생성(starbucks, BigDecimal.valueOf(8_000));

        final Brand ediya = EDIYA.생성();
        final Product ediyaCoffee1 = COFFEE.브랜드_가격_설정_생성(ediya, BigDecimal.valueOf(9_000));
        final Product ediyaCoffee2 = COFFEE.브랜드_가격_설정_생성(ediya, BigDecimal.valueOf(10_000));

        productRepository.save(starbucksCoffee1);
        productRepository.save(starbucksCoffee2);
        productRepository.save(ediyaCoffee1);
        productRepository.save(ediyaCoffee2);

        final String keyword = "커피";
        final Pageable pageable = PageRequest.of(0, 4, Sort.by("price").ascending());
        final Slice<Product4DisplayDto> slice = productRepository.findBySearchConditions(keyword, null, null, null, pageable);

        final List<Product4DisplayDto> products = slice.getContent();
        assertThat(products.size()).isEqualTo(4);
        assertThat(products).usingRecursiveComparison().comparingOnlyFields("name").isEqualTo(
                List.of(
                        getProduct4DisplayDto(starbucksCoffee1),
                        getProduct4DisplayDto(starbucksCoffee2),
                        getProduct4DisplayDto(ediyaCoffee1),
                        getProduct4DisplayDto(ediyaCoffee2)
                )
        );
    }

    @Test
    @DisplayName("상품명으로 조회 높은 가격 순")
    public void findBySearchConditionsOrderByPriceDesc() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCoffee1 = COFFEE.브랜드_가격_설정_생성(starbucks, BigDecimal.valueOf(10_000));
        final Product starbucksCoffee2 = COFFEE.브랜드_가격_설정_생성(starbucks, BigDecimal.valueOf(9_000));

        final Brand ediya = EDIYA.생성();
        final Product ediyaCoffee1 = COFFEE.브랜드_가격_설정_생성(ediya, BigDecimal.valueOf(8_000));
        final Product ediyaCoffee2 = COFFEE.브랜드_가격_설정_생성(ediya, BigDecimal.valueOf(7_000));

        productRepository.save(starbucksCoffee1);
        productRepository.save(starbucksCoffee2);
        productRepository.save(ediyaCoffee1);
        productRepository.save(ediyaCoffee2);

        final String keyword = "커피";
        final Pageable pageable = PageRequest.of(0, 4, Sort.by("price").ascending());
        final Slice<Product4DisplayDto> slice = productRepository.findBySearchConditions(keyword, null, null, null, pageable);

        final List<Product4DisplayDto> products = slice.getContent();
        assertThat(products.size()).isEqualTo(4);
        assertThat(products).usingRecursiveComparison().comparingOnlyFields("name").isEqualTo(
                List.of(
                        getProduct4DisplayDto(starbucksCoffee1),
                        getProduct4DisplayDto(starbucksCoffee2),
                        getProduct4DisplayDto(ediyaCoffee1),
                        getProduct4DisplayDto(ediyaCoffee2)
                )
        );
    }

    @Test
    @DisplayName("상품명으로 조회 후 브랜드별 그룹핑")
    public void findBySearchConditionsGroupByBrand() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCoffee1 = COFFEE.브랜드_설정_생성(starbucks);
        final Product starbucksCoffee2 = COFFEE.브랜드_설정_생성(starbucks);

        final Brand ediya = EDIYA.생성();
        final Product ediyaCoffee1 = COFFEE.브랜드_설정_생성(ediya);
        final Product ediyaCoffee2 = COFFEE.브랜드_설정_생성(ediya);

        productRepository.save(starbucksCoffee1);
        productRepository.save(starbucksCoffee2);
        productRepository.save(ediyaCoffee1);
        productRepository.save(ediyaCoffee2);

        final String keyword = "커피";
        final Pageable pageable = PageRequest.of(0, 4, Sort.unsorted());
        final Slice<SimpleBrandProductDto> slice = productRepository.findBySearchConditionsGroupByBrand(keyword, pageable);

        final List<SimpleBrandProductDto> brandProducts = slice.getContent();
        assertThat(brandProducts.size()).isEqualTo(2);
        assertThat(brandProducts).usingRecursiveComparison().comparingOnlyFields("name").isEqualTo(
                List.of(getSimpleBrandProductDto(starbucks, starbucksCoffee1, starbucksCoffee2),
                        getSimpleBrandProductDto(ediya, ediyaCoffee1, ediyaCoffee2)
                )
        );
    }

    private Product4DisplayDto getProduct4DisplayDto(final Product product) {
        return new Product4DisplayDto(product.getProductId(), product.getName(), product.getPhoto(), product.getPrice(), product.getBrand().getName(), null);
    }

    private SimpleBrandDto getSimpleBrandDto(final Brand brand) {
        return new SimpleBrandDto(brand.getBrandId(), brand.getName(), brand.getIconPhoto());
    }


    private SimpleBrandProductDto getSimpleBrandProductDto(final Brand brand, final Product... products) {
        final List<Product4DisplayDto> product4DisplayDtos = Arrays.stream(products)
                .map(this::getProduct4DisplayDto)
                .toList();
        return new SimpleBrandProductDto(getSimpleBrandDto(brand), product4DisplayDtos);
    }
}