package org.kakaoshare.backend.domain.gift.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.controller.GiftStatusConstraint;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.request.FundingGiftHistoryRequest;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.FundingGiftHistoryResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.repository.FundingGiftRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FundingGiftServiceTest {
    @Mock
    private FundingGiftRepository fundingGiftRepository;

    @InjectMocks
    private FundingGiftService fundingGiftService;

    private String providerId;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        providerId = "providerId";
        pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
    }

    @ParameterizedTest
    @DisplayName("나의 펀딩 선물함 조회 (상태 필터링 O)")
    @ValueSource(strings = {"USABLE", "USED"})
    public void lookUpWithStatus(final String statusParam) throws Exception {
        final FundingGiftHistoryRequest fundingGiftHistoryRequest = new FundingGiftHistoryRequest(statusParam);
        final Product cake = CAKE.생성();
        final Page<?> page = getPage(cake);
        final List<GiftStatus> statuses = GiftStatusConstraint.findByParam(statusParam);
        doReturn(page).when(fundingGiftRepository).findHistoryByCondition(providerId, statuses, pageable);

        final PageResponse<?> actual = fundingGiftService.lookUp(providerId, fundingGiftHistoryRequest, pageable);
        final PageResponse<?> expect = PageResponse.from(page);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("나의 펀딩 선물함 조회 (상태 필터링 X, 전체)")
    public void lookUpWithoutStatus() throws Exception {
        final FundingGiftHistoryRequest fundingGiftHistoryRequest = new FundingGiftHistoryRequest(null);
        final Product cake = CAKE.생성();
        final Page<?> page = getPage(cake);
        doReturn(page).when(fundingGiftRepository).findHistoryByConditionWithoutStatus(providerId, pageable);

        final PageResponse<?> actual = fundingGiftService.lookUp(providerId, fundingGiftHistoryRequest, pageable);
        final PageResponse<?> expect = PageResponse.from(page);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    private Page<?> getPage(final Product product) {
        return new PageImpl<>(getFundingGiftHistoryResponsesSameStatus(product));
    }
    private List<FundingGiftHistoryResponse> getFundingGiftHistoryResponsesSameStatus(final Product product) {
        return List.of(
                getFundingGiftHistoryResponse(product),
                getFundingGiftHistoryResponse(product),
                getFundingGiftHistoryResponse(product),
                getFundingGiftHistoryResponse(product)
        );
    }

    private FundingGiftHistoryResponse getFundingGiftHistoryResponse(final Product product) {
        return new FundingGiftHistoryResponse(null, getProductDto(product), 1, LocalDateTime.now());
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
}