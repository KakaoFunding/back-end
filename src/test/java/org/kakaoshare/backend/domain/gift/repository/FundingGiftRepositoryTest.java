package org.kakaoshare.backend.domain.gift.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.FundingGiftHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class FundingGiftRepositoryTest {
    @Autowired
    private FundingGiftRepository fundingGiftRepository;

    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
    }

    @ParameterizedTest
    @DisplayName("나의 펀딩 선물함 조회 (상태 필터링 O)")
    @CsvSource(value = {
            "providerId1,USED",
            "providerId1,USING",
            "providerId2,USED",
            "providerId2,USING",
            "providerId3,USED",
            "providerId3,USING"
    })
    public void findHistoryByStatus(final String providerId, final String status) throws Exception {
        final Page<FundingGiftHistoryResponse> page = fundingGiftRepository.findHistoryByCondition(providerId, status, pageable);
        final boolean isAllSameStatus = page.getContent()
                .stream()
                .allMatch(fundingGiftHistoryResponse -> fundingGiftHistoryResponse.status().equals(status));
        assertThat(isAllSameStatus).isTrue();
    }

    @ParameterizedTest
    @DisplayName("나의 펀딩 선물함 조회 (상태 필터링 X, 전체)")
    @ValueSource(strings = {"providerId1", "providerId2", "providerId3"})
    public void findAllHistory(final String providerId) throws Exception {
        final Page<FundingGiftHistoryResponse> page = fundingGiftRepository.findHistoryByConditionWithoutStatus(providerId, pageable);
        assertThat(page.getNumberOfElements()).isEqualTo(3);
    }
}