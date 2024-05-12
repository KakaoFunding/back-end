package org.kakaoshare.backend.domain.funding.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.common.vo.Date;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryResponse;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus.COMPLETE;
import static org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus.PROGRESS;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;
import static org.kakaoshare.backend.fixture.MemberFixture.KIM;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.kakaoshare.backend.fixture.ProductFixture.COFFEE;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class FundingServiceTest {

    @InjectMocks
    private FundingService fundingService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FundingRepository fundingRepository;

    @Mock
    private FundingDetailRepository fundingDetailRepository;

    Member contributor;
    Member creator;

    @BeforeEach
    public void setUp() {
        contributor = KAKAO.생성();
        creator = KIM.생성();
    }

    @ParameterizedTest
    @DisplayName("내가 기여한 펀딩 내역 조회 (상태 필터링 O)")
    @ValueSource(strings = {"PROGRESS", "COMPLETE", "CANCEL_REFUND"})
    public void lookUp(final String status) throws Exception {
        final String providerId = contributor.getProviderId();
        final Date date = new Date(
                LocalDate.now().minusMonths(5L),
                LocalDate.now()
        );
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final List<ContributedFundingHistoryResponse> content = List.of(
                new ContributedFundingHistoryResponse(
                        getProductDto(cake),
                        getFundingHistoryDto(1L, 1L, LocalDateTime.now().minusDays(20L), creator.getName(), status)
                ),
                new ContributedFundingHistoryResponse(
                        getProductDto(coffee),
                        getFundingHistoryDto(2L, 2L, LocalDateTime.now().minusDays(20L), creator.getName(), status)
                )
        );
        final Page<?> page = new PageImpl<>(content, pageable, content.size());
        doReturn(page).when(fundingDetailRepository).findHistoryByProviderIdAndDateAndStatus(providerId, date, status, pageable);

        final PageResponse<?> actual = fundingService.lookUp(providerId, date, status, pageable);
        final PageResponse<?> expect = PageResponse.from(page);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("내가 기여한 펀딩 내역 조회 (상태 필터링 X)")
    public void lookUpWithoutStatus() throws Exception {
        final String providerId = contributor.getProviderId();
        final Date date = new Date(
                LocalDate.now().minusMonths(5L),
                LocalDate.now()
        );
        final String status = null;
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final List<ContributedFundingHistoryResponse> content = List.of(
                new ContributedFundingHistoryResponse(
                        getProductDto(cake),
                        getFundingHistoryDto(1L, 1L, LocalDateTime.now().minusDays(20L), creator.getName(), COMPLETE.name())
                ),
                new ContributedFundingHistoryResponse(
                        getProductDto(coffee),
                        getFundingHistoryDto(2L, 2L, LocalDateTime.now().minusDays(20L), creator.getName(), PROGRESS.name()
                        )
                ));
        final Page<?> page = new PageImpl<>(content, pageable, content.size());
        doReturn(page).when(fundingDetailRepository).findHistoryByProviderIdAndDate(providerId, date, pageable);

        final PageResponse<?> actual = fundingService.lookUp(providerId, date, status, pageable);
        final PageResponse<?> expect = PageResponse.from(page);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

//    @Test
//    @DisplayName("성공적으로 펀딩 아이템 등록")
//    void registerFundingItem_Success() {
//        Long productId = 1L;
//        String providerId = "provider123";
//        RegisterRequest request = RegisterRequest.builder()
//                .goalAmount(new BigDecimal("1000"))
//                .expiredAt(LocalDate.now().plusDays(30))
//                .build();
//
//        Product product = ProductFixture.TEST_PRODUCT.생성();
//        Member member = MemberFixture.KAKAO.생성();
//        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(member, product);
//
//        given(productRepository.findById(productId)).willReturn(Optional.of(product));
//        given(memberRepository.findMemberByProviderId(providerId)).willReturn(Optional.of(member));
//        given(fundingRepository.findByIdAndMemberId(any(), any())).willReturn(Optional.empty());
//        given(fundingRepository.save(any(Funding.class))).willReturn(funding);
//
//        RegisterResponse response = fundingService.registerFundingItem(productId, providerId, request);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getId()).isEqualTo(funding.getFundingId());
//    }
//
//    @Test
//    @DisplayName("펀딩 진행 상황 조회 성공")
//    void getFundingProgress_Success() {
//        Long fundingId = 1L;
//        Member member = MemberFixture.KAKAO.생성();
//        Product product = ProductFixture.TEST_PRODUCT.생성();
//        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(member, product);
//
//        given(memberRepository.findMemberByProviderId(member.getProviderId())).willReturn(Optional.of(member));
//        given(fundingRepository.findByIdAndMemberId(fundingId, member.getMemberId())).willReturn(Optional.of(funding));
//
//        ProgressResponse response = fundingService.getFundingProgress(fundingId, member.getProviderId());
//
//        assertThat(response).isNotNull();
//
//        verify(memberRepository).findMemberByProviderId(member.getProviderId());
//        verify(fundingRepository).findByIdAndMemberId(fundingId, member.getMemberId());
//    }
//
//    @Test
//    @DisplayName("내가 등록한 펀딩아이템 조회 성공")
//    void getMyAllFundingItems_Success() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Member member = MemberFixture.KAKAO.생성();
//        Brand brand = BrandFixture.BRAND_C.생성();
//        Product product = ProductFixture.TEST_PRODUCT.생성(brand);
//        List<Funding> fundingList = Arrays.asList(FundingFixture.SAMPLE_FUNDING.생성(member, product),
//                FundingFixture.SAMPLE_FUNDING2.생성(member, product));
//        Slice<Funding> fundingSlice = new SliceImpl<>(fundingList, pageable, false);
//
//        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
//        when(fundingRepository.findAllByMemberId(member.getMemberId())).thenReturn(fundingList);
//        when(fundingRepository.findFundingByMemberIdWithSlice(member.getMemberId(), pageable)).thenReturn(fundingSlice);
//
//        FundingSliceResponse response = fundingService.getMyAllFundingProducts(member.getProviderId(), pageable);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getFundingItems()).hasSize(fundingList.size());
//    }

    public ProductDto getProductDto(final Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getPhoto(),
                product.getPrice(),
                product.getBrandName()
        );
    }

    public ContributedFundingHistoryDto getFundingHistoryDto(final Long fundingId,
                                                             final Long fundingDetailId,
                                                             final LocalDateTime attributedAt,
                                                             final String creatorName, final String status) {
        return new ContributedFundingHistoryDto(fundingId, fundingDetailId, attributedAt, creatorName, status);
    }
}
