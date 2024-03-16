package org.kakaoshare.backend.domain.funding.service;

import static org.mockito.ArgumentMatchers.any;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegistrationResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.fixture.FundingFixture;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

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
        RegisterRequest request = RegisterRequest.builder()
                .goalAmount(new BigDecimal("1000"))
                .expiredAt(LocalDate.now().plusDays(30))
                .build();

        Product product = ProductFixture.TEST_PRODUCT.생성();
        Member member = MemberFixture.KAKAO.생성();
        Funding funding = FundingFixture.SAMPLE_FUNDING.생성(member, product);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(memberRepository.findByProviderId(providerId)).willReturn(Optional.of(member));
        given(fundingRepository.findByIdAndMemberId(any(), any())).willReturn(Optional.empty());
        given(fundingRepository.save(any(Funding.class))).willReturn(funding);

        RegistrationResponse response = fundingService.registerFundingItem(productId, providerId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(funding.getFundingId());
    }
}
