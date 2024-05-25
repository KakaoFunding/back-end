package org.kakaoshare.backend.domain.funding.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.ContributedFundingHistoryRequest;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
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
public class FundingDetailServiceTest {
    @Mock
    private FundingDetailRepository fundingDetailRepository;

    @InjectMocks
    private FundingDetailService fundingDetailService;

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
        final LocalDate startDate = LocalDate.now().minusMonths(5L);
        final LocalDate endDate = LocalDate.now();
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        final FundingHistoryDate date = contributedFundingHistoryRequest.toDate();
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
        doReturn(page).when(fundingDetailRepository).findHistoryByCondition(providerId, date, status, pageable);

        final PageResponse<?> actual = fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable);
        final PageResponse<?> expect = PageResponse.from(page);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("내가 기여한 펀딩 내역 조회 (상태 필터링 X)")
    public void lookUpWithoutStatus() throws Exception {
        final String providerId = contributor.getProviderId();
        final LocalDate startDate = LocalDate.now().minusMonths(5L);
        final LocalDate endDate = LocalDate.now();
        final String status = null;
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        final FundingHistoryDate date = contributedFundingHistoryRequest.toDate();
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
        doReturn(page).when(fundingDetailRepository).findHistoryByConditionWithoutStatus(providerId, date, pageable);

        final PageResponse<?> actual = fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable);
        final PageResponse<?> expect = PageResponse.from(page);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    private ProductDto getProductDto(final Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getPhoto(),
                product.getPrice(),
                product.getBrandName()
        );
    }

    private ContributedFundingHistoryDto getFundingHistoryDto(final Long fundingId,
                                                             final Long fundingDetailId,
                                                             final LocalDateTime contributedAt,
                                                             final String creatorName, final String status) {
        return new ContributedFundingHistoryDto(fundingId, fundingDetailId, 1000L, contributedAt, creatorName, status);
    }
}
