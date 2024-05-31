package org.kakaoshare.backend.domain.gift.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.FundingGiftHistoryResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.CANCEL_REFUND;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.NOT_USED;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.USED;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.USING;

@RepositoryTest
class FundingGiftRepositoryTest {
    @Autowired
    private FundingGiftRepository fundingGiftRepository;

    private Pageable pageable;

    static Stream<Arguments> statusData() {
        return Stream.of(
                Arguments.of("providerId1", List.of(USING, NOT_USED)),
                Arguments.of("providerId1", List.of(USED, CANCEL_REFUND)),
                Arguments.of("providerId2", List.of(USING, NOT_USED)),
                Arguments.of("providerId2", List.of(USED, CANCEL_REFUND)),
                Arguments.of("providerId3", List.of(USING, NOT_USED)),
                Arguments.of("providerId3", List.of(USED, CANCEL_REFUND))
        );
    }

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
    }

    @ParameterizedTest
    @DisplayName("나의 펀딩 선물함 조회 (상태 필터링 O)")
    @MethodSource("statusData")
    public void findHistoryByStatus(final String providerId, final List<GiftStatus> statuses) throws Exception {
        final Page<FundingGiftHistoryResponse> page = fundingGiftRepository.findHistoryByCondition(providerId, statuses, pageable);
        assertThat(page.getNumberOfElements()).isEqualTo(1);
    }

    @ParameterizedTest
    @DisplayName("나의 펀딩 선물함 조회 (상태 필터링 X, 전체)")
    @ValueSource(strings = {"providerId1", "providerId2", "providerId3"})
    public void findAllHistory(final String providerId) throws Exception {
        final Page<FundingGiftHistoryResponse> page = fundingGiftRepository.findHistoryByConditionWithoutStatus(providerId, pageable);
        assertThat(page.getNumberOfElements()).isEqualTo(2);
    }
}