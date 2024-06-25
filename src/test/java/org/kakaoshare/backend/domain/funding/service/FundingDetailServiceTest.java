package org.kakaoshare.backend.domain.funding.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.common.vo.date.exception.DateException;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.ContributedFundingHistoryRequest;
import org.kakaoshare.backend.domain.funding.dto.inquiry.response.ContributedFundingHistoryResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    Pageable pageable;

    @BeforeEach
    public void setUp() {
        contributor = KAKAO.생성();
        creator = KIM.생성();
        pageable = PageRequest.of(0, 5, Sort.by("createdAt"));
    }

    @ParameterizedTest
    @DisplayName("내가 기여한 펀딩 내역 조회 (상태 필터링 O)")
    @ValueSource(strings = {"PROGRESS", "COMPLETE", "CANCEL_REFUND"})
    public void lookUp(final String status) throws Exception {
        final String providerId = contributor.getProviderId();
        final LocalDate startDate = LocalDate.now().minusMonths(5L);
        final LocalDate endDate = LocalDate.now();

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        final FundingHistoryDate date = contributedFundingHistoryRequest.toDate();
        final List<ContributedFundingHistoryDto> content = List.of(
                getFundingHistoryDto(getProductDto(cake), 1L, 1L, LocalDateTime.now().minusDays(20L), providerId, creator.getName(), status),
                getFundingHistoryDto(getProductDto(coffee), 2L, 2L, LocalDateTime.now().minusDays(20L), providerId, creator.getName(), status)
        );
        final Page<ContributedFundingHistoryDto> page = new PageImpl<>(content, pageable, content.size());
        doReturn(page).when(fundingDetailRepository).findHistoryByCondition(providerId, date, status, pageable);

        final Page<ContributedFundingHistoryResponse> contributedFundingHistoryResponses = page.map(historyDto -> ContributedFundingHistoryResponse.of(historyDto, providerId));

        final PageResponse<?> actual = fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable);
        final PageResponse<?> expect = PageResponse.from(contributedFundingHistoryResponses);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @ParameterizedTest
    @DisplayName("내가 기여한 펀딩 내역 조회 (상태 필터링 X)")
    @NullSource
    @EmptySource
    public void lookUpWithoutStatus(final String status) throws Exception {
        final String providerId = contributor.getProviderId();
        final LocalDate startDate = LocalDate.now().minusMonths(5L);
        final LocalDate endDate = LocalDate.now();

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        final FundingHistoryDate date = contributedFundingHistoryRequest.toDate();
        final List<ContributedFundingHistoryDto> content = List.of(
                getFundingHistoryDto(getProductDto(cake), 1L, 1L, LocalDateTime.now().minusDays(20L), providerId, creator.getName(), COMPLETE.name()),
                getFundingHistoryDto(getProductDto(coffee), 2L, 2L, LocalDateTime.now().minusDays(20L), providerId, creator.getName(), PROGRESS.name())
        );
        final Page<ContributedFundingHistoryDto> page = new PageImpl<>(content, pageable, content.size());
        doReturn(page).when(fundingDetailRepository).findHistoryByConditionWithoutStatus(providerId, date, pageable);

        final Page<ContributedFundingHistoryResponse> contributedFundingHistoryResponses = page.map(historyDto -> ContributedFundingHistoryResponse.of(historyDto, providerId));

        final PageResponse<?> actual = fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable);
        final PageResponse<?> expect = PageResponse.from(contributedFundingHistoryResponses);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @ParameterizedTest
    @DisplayName("내가 기여한 내역 조회 시 조회 시작, 종료일이 모두 NULL이면 예외 발생")
    @NullSource
    @EmptySource
    @ValueSource(strings = {"PROGRESS", "COMPLETE", "CANCEL_REFUND"})
    public void lookUpWhenDateIsNull(final String status) throws Exception {
        final String providerId = contributor.getProviderId();
        final LocalDate startDate = null;
        final LocalDate endDate = null;
        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        assertThatThrownBy(() -> fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable))
                .isInstanceOf(DateException.class);
    }

    @ParameterizedTest
    @DisplayName("내가 기여한 내역 조회 시 종료일이 시작일보다 앞서면 예외 발생")
    @NullSource
    @EmptySource
    @ValueSource(strings = {"PROGRESS", "COMPLETE", "CANCEL_REFUND"})
    public void lookUpWhenInvalidDate(final String status) throws Exception {
        final String providerId = contributor.getProviderId();
        final LocalDate startDate = LocalDate.of(2024, 2, 1);
        final LocalDate endDate = LocalDate.of(2024, 1, 1);
        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        assertThatThrownBy(() -> fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable))
                .isInstanceOf(DateException.class);
    }

    @ParameterizedTest
    @DisplayName("내가 기여한 내역 조회 기간이 1년이 넘으면 예외 발생")
    @NullSource
    @EmptySource
    @ValueSource(strings = {"PROGRESS", "COMPLETE", "CANCEL_REFUND"})
    public void lookUpWhenInvalidDateRange(final String status) throws Exception {
        final String providerId = contributor.getProviderId();
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final LocalDate endDate = LocalDate.of(2024, 9, 1);
        final ContributedFundingHistoryRequest contributedFundingHistoryRequest = new ContributedFundingHistoryRequest(startDate, endDate, status);
        assertThatThrownBy(() -> fundingDetailService.lookUp(providerId, contributedFundingHistoryRequest, pageable))
                .isInstanceOf(DateException.class);
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

    private ContributedFundingHistoryDto getFundingHistoryDto(final ProductDto productDto,
                                                              final Long fundingId,
                                                              final Long fundingDetailId,
                                                              final LocalDateTime contributedAt,
                                                              final String creatorName,
                                                              final String providerId,
                                                              final String status) {
        return new ContributedFundingHistoryDto(productDto, fundingId, fundingDetailId, 1000L, contributedAt, providerId, creatorName, status);
    }
}
