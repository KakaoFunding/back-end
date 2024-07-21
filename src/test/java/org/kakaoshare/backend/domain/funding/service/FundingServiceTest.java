package org.kakaoshare.backend.domain.funding.service;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.funding.dto.FundingResponse;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
import org.kakaoshare.backend.domain.funding.exception.FundingException;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.fixture.BrandFixture;
import org.kakaoshare.backend.fixture.FundingFixture;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;


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

    @Test
    @DisplayName("성공적으로 펀딩 아이템 등록")
    void registerFundingItem_Success() {
        Long productId = 1L;
        String providerId = "provider123";
        RegisterRequest request = new RegisterRequest(999L, LocalDate.now().plusDays(30));

        Product product = ProductFixture.TEST_PRODUCT.생성();
        Member member = MemberFixture.KAKAO.생성();
        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(member, product);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(memberRepository.findMemberByProviderId(providerId)).willReturn(Optional.of(member));
        given(fundingRepository.save(any(Funding.class))).willReturn(funding);

        RegisterResponse response = fundingService.registerFundingItem(productId, providerId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(funding.getFundingId());
    }

    @Test
    @DisplayName("펀딩 아이템 등록 실패 - 금액 유효성 검증 실패")
    void registerFundingItem_Fail() {
        Long productId = 1L;
        String providerId = "provider123";
        RegisterRequest request = new RegisterRequest(900L , LocalDate.now().plusDays(30));

        Product product = ProductFixture.TEST_PRODUCT.생성();
        Member member = MemberFixture.KAKAO.생성();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(memberRepository.findMemberByProviderId(providerId)).willReturn(Optional.of(member));

        assertThatThrownBy(() -> fundingService.registerFundingItem(productId, providerId, request))
                .isInstanceOf(FundingException.class);

        verify(fundingRepository, never()).save(any(Funding.class));
    }

    @Test
    @DisplayName("펀딩 진행 상황 조회 성공")
    void getFundingProgress_Success() {
        Brand brand = BrandFixture.EDIYA.생성(1L);
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L, brand);
        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(1L, member, product);

        given(memberRepository.findMemberByProviderId(member.getProviderId())).willReturn(Optional.of(member));
        given(fundingRepository.findByIdAndMemberId(funding.getFundingId(), member.getMemberId())).willReturn(
                Optional.of(funding));

        ProgressResponse response = fundingService.getFundingItemProgress(funding.getFundingId(),
                member.getProviderId());

        assertThat(response).isNotNull();

        verify(memberRepository).findMemberByProviderId(member.getProviderId());
    }

    @Test
    @DisplayName("나의 등록된 펀딩아이템 조회")
    public void testGetMyFundingProgress_WithValidData() {
        Brand brand = BrandFixture.EDIYA.생성(1L);
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L, brand);
        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(1L, member, product);

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(fundingRepository.findByMemberIdAndStatus(member.getMemberId(), FundingStatus.PROGRESS)).thenReturn(
                Optional.of(funding));
        when(fundingRepository.findByIdAndMemberId(funding.getFundingId(), member.getMemberId())).thenReturn(
                Optional.of(funding));

        ProgressResponse response = fundingService.getMyFundingProgress(member.getProviderId());
        assertNotNull(response, "ProgressResponse should not be null");
    }

    @Test
    @DisplayName("내가 등록했던 펀딩아이템 목록 조회")
    public void testGetMyAllFundingItems() {
        Pageable pageable = PageRequest.of(0, 5);
        Brand brand = BrandFixture.EDIYA.생성(1L);
        Member member = MemberFixture.KAKAO.생성();
        Product product1 = ProductFixture.TEST_PRODUCT.생성(1L, brand);
        Product product2 = ProductFixture.TEST_PRODUCT.생성(2L, brand);
        Product product3 = ProductFixture.TEST_PRODUCT.생성(3L, brand);
        Funding funding1 = FundingFixture.SAMPLE_FUNDING.생성(1L, member, product1, FundingStatus.COMPLETE);
        Funding funding2 = FundingFixture.SAMPLE_FUNDING.생성(2L, member, product2, FundingStatus.PROGRESS);
        Funding funding3 = FundingFixture.SAMPLE_FUNDING.생성(3L, member, product3, FundingStatus.CANCEL);

        List<FundingResponse> fundingResponses = Arrays.asList(
                FundingResponse.from(funding1),
                FundingResponse.from(funding2),
                FundingResponse.from(funding3)
        );
        Page<FundingResponse> fundingPage = new PageImpl<>(fundingResponses, pageable, fundingResponses.size());

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberIdAndStatusWithPage(
                eq(member.getMemberId()), nullable(FundingStatus.class), eq(pageable)))
                .thenReturn(fundingPage);

        PageResponse<?> response = fundingService.getMyFilteredFundingProducts(member.getProviderId(), null, pageable);

        assertThat(response.getItems()).hasSize(3);
        verify(fundingRepository).findFundingByMemberIdAndStatusWithPage(member.getMemberId(), null, pageable);
    }

    @Test
    @DisplayName("내가 등록했던 펀딩아이템 목록 조회 - CANCEL 상태만")
    public void testGetMyFilteredFundingItemsForCancelStatus() {
        Pageable pageable = PageRequest.of(0, 5);
        Brand brand = BrandFixture.EDIYA.생성(1L);
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L, brand);
        Funding funding1 = FundingFixture.SAMPLE_FUNDING.생성(1L, member, product, FundingStatus.CANCEL);
        Funding funding2 = FundingFixture.SAMPLE_FUNDING.생성(2L, member, product, FundingStatus.COMPLETE);
        Funding funding3 = FundingFixture.SAMPLE_FUNDING.생성(3L, member, product, FundingStatus.PROGRESS);

        List<FundingResponse> fundingResponses = Arrays.asList(
                FundingResponse.from(funding1),
                FundingResponse.from(funding2),
                FundingResponse.from(funding3)
        );
        Page<FundingResponse> allFundingsPage = new PageImpl<>(fundingResponses, pageable, fundingResponses.size());
        Page<FundingResponse> cancelFundingsPage = new PageImpl<>(List.of(FundingResponse.from(funding1)), pageable, 1);

        when(memberRepository.findMemberByProviderId(member.getProviderId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberIdAndStatusWithPage(
                eq(member.getMemberId()), eq(FundingStatus.CANCEL), eq(pageable)))
                .thenReturn(cancelFundingsPage);

        PageResponse<?> response = fundingService.getMyFilteredFundingProducts(member.getProviderId(), FundingStatus.CANCEL, pageable);

        assertThat(response.getItems()).hasSize(1);
        verify(fundingRepository).findFundingByMemberIdAndStatusWithPage(member.getMemberId(), FundingStatus.CANCEL, pageable);
    }


}

