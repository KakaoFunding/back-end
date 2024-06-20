package org.kakaoshare.backend.domain.funding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
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
        RegisterRequest request = new RegisterRequest(1000L, LocalDate.now().plusDays(30));

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
    @DisplayName("펀딩 진행 상황 조회 성공")
    void getFundingProgress_Success() {
        Brand brand = BrandFixture.EDIYA.생성(1L);
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성(1L, brand);
        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(1L, member, product);

        given(memberRepository.findMemberByProviderId(member.getProviderId())).willReturn(Optional.of(member));
        given(fundingRepository.findByIdAndMemberId(funding.getFundingId(), member.getMemberId())).willReturn(Optional.of(funding));

        ProgressResponse response = fundingService.getFundingItemProgress(funding.getFundingId(), member.getProviderId());

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
        when(fundingRepository.findByMemberIdAndStatus(member.getMemberId(), FundingStatus.PROGRESS)).thenReturn(Optional.of(funding));
        when(fundingRepository.findByIdAndMemberId(funding.getFundingId(),member.getMemberId())).thenReturn(Optional.of(funding));

        ProgressResponse response = fundingService.getMyFundingProgress(member.getProviderId());
        assertNotNull(response, "ProgressResponse should not be null");
    }
}
