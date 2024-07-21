package org.kakaoshare.backend.domain.search.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.brand.dto.SimpleBrandDto;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.brand.repository.BrandRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.Product4DisplayDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.search.dto.BrandSearchRequest;
import org.kakaoshare.backend.domain.search.dto.ProductSearchRequest;
import org.kakaoshare.backend.domain.search.dto.SimpleBrandProductDto;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.BrandFixture.EDIYA;
import static org.kakaoshare.backend.fixture.BrandFixture.STARBUCKS;
import static org.kakaoshare.backend.fixture.ProductFixture.COFFEE;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;
    
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("상품 검색 결과")
    public void searchProducts() throws Exception {
        final Product coffee1 = COFFEE.생성();
        final Product coffee2 = COFFEE.생성();
        final Pageable pageable = Pageable.unpaged();

        final ProductSearchRequest request = new ProductSearchRequest("커피", null, null, null);
        final List<Product4DisplayDto> product4DisplayDtos = List.of(
                getProduct4DisplayDto(coffee1, null),
                getProduct4DisplayDto(coffee2, null)
        );
        Member member = MemberFixture.KAKAO.생성();
        final Page<Product4DisplayDto> page = new PageImpl<>(product4DisplayDtos, pageable, product4DisplayDtos.size());
        doReturn(page).when(productRepository).findBySearchConditions(
                request.keyword(),
                request.minPrice(),
                request.maxPrice(),
                request.categories(),
                pageable,
                member.getProviderId()
        );
        final PageResponse<?> expect = PageResponse.from(page);
        final PageResponse<?> actual = searchService.searchProducts(request, pageable, member.getProviderId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("상품 검색 결과 가격 범위 필터링")
    public void searchProductsFilterByPrice() throws Exception {
        final Product coffee1 = COFFEE.가격_설정_생성(10_000L);
        final Product coffee2 = COFFEE.가격_설정_생성(10_000L);

        final int minPrice = 3_000;
        final int maxPrice = 9_000;
        final Pageable pageable = Pageable.unpaged();
        final ProductSearchRequest request = new ProductSearchRequest("커피", null, minPrice, maxPrice);
        final List<Product4DisplayDto> product4DisplayDtos = List.of(
                getProduct4DisplayDto(coffee1, null),
                getProduct4DisplayDto(coffee2, null)
        );
        
        Member member = MemberFixture.KAKAO.생성();
        
        final Page<Product4DisplayDto> page = new PageImpl<>(product4DisplayDtos, pageable, product4DisplayDtos.size());
        doReturn(page).when(productRepository).findBySearchConditions(
                request.keyword(),
                request.minPrice(),
                request.maxPrice(),
                request.categories(),
                pageable,
                member.getProviderId()
        );
        final PageResponse<?> expect = PageResponse.from(page);
        final PageResponse<?> actual = searchService.searchProducts(request, pageable, member.getProviderId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("브랜드 검색 결과")
    public void searchBrands() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Brand ediya = EDIYA.생성();
        final Product starbucksCoffee = COFFEE.생성(1L, starbucks);
        final Pageable pageable = Pageable.unpaged();

        final BrandSearchRequest request = new BrandSearchRequest("커피");
        final List<SimpleBrandDto> expect = List.of(getSimpleBrandDto(starbucks));
        doReturn(expect).when(brandRepository).findBySearchConditions(request.keyword(), pageable);

        final List<SimpleBrandDto> actual = searchService.searchBrands(request, pageable);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("브랜드별 상품 검색 결과")
    public void searchProductsGroupByBrand() throws Exception {
        final Brand starbucks = STARBUCKS.생성();
        final Product starbucksCoffee1 = COFFEE.생성(1L, starbucks);
        final Product starbucksCoffee2 = COFFEE.생성(2L, starbucks);

        final Brand ediya = EDIYA.생성();
        final Product ediyasCoffee1 = COFFEE.생성(3L, ediya);
        final Product ediyasCoffee2 = COFFEE.생성(4L, ediya);
        
        Member member = MemberFixture.KAKAO.생성();
        
        
        final Pageable pageable = Pageable.unpaged();
        final BrandSearchRequest request = new BrandSearchRequest("커피");
        final List<SimpleBrandProductDto> simpleBrandProductDtos = List.of(
                getSimpleBrandProductDto(starbucks, starbucksCoffee1, starbucksCoffee2),
                getSimpleBrandProductDto(ediya, ediyasCoffee1, ediyasCoffee2)
        );
        final Page<SimpleBrandProductDto> page = new PageImpl<>(simpleBrandProductDtos, pageable, simpleBrandProductDtos.size());
        doReturn(page).when(productRepository).findBySearchConditionsGroupByBrand(request.keyword(), pageable,member.getProviderId());

        final PageResponse<?> expect = PageResponse.from(page);
        final PageResponse<?> actual = searchService.searchProductGroupByBrand(request, pageable, member.getProviderId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    private Product4DisplayDto getProduct4DisplayDto(final Product product) {
        return new Product4DisplayDto(product.getProductId(), product.getName(), product.getPhoto(), product.getPrice(), product.getBrand().getName(), null,false);
    }

    private Product4DisplayDto getProduct4DisplayDto(final Product product, final String brandName) {
        return new Product4DisplayDto(product.getProductId(), product.getName(), product.getPhoto(), product.getPrice(), brandName, null,false);
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