package org.kakaoshare.backend.domain.gift.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.GiftDto;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.CANCEL_REFUND;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.NOT_USED;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.USED;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.USING;

@RepositoryTest
public class GiftRepositoryTest {
    @Autowired
    private GiftRepository giftRepository;
    private Pageable pageable;

    static Stream<Arguments> statusData() {
        return Stream.of(
                Arguments.of("providerId1", USING),
                Arguments.of("providerId1", NOT_USED),
                Arguments.of("providerId1", USED),
                Arguments.of("providerId1", CANCEL_REFUND),
                Arguments.of("providerId2", USING),
                Arguments.of("providerId2", NOT_USED),
                Arguments.of("providerId2", USED),
                Arguments.of("providerId2", CANCEL_REFUND),
                Arguments.of("providerId3", USING),
                Arguments.of("providerId3", NOT_USED),
                Arguments.of("providerId3", USED),
                Arguments.of("providerId3", CANCEL_REFUND)
        );
    }

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
    }

    @ParameterizedTest
    @DisplayName("선물함 조회")
    @MethodSource("statusData")
    public void findGiftsByMemberIdAndStatusTest(final String providerId, final GiftStatus status) {
        final Page<GiftDto> page = giftRepository.findHistoryByProviderIdAndStatus(providerId, status, pageable);
        assertThat(page.hasContent()).isTrue();
    }

    @Test
    @DisplayName("특정 사용자의 선물함 조회 - 사용하지 않음")
    public void findGiftsByMemberId1AndStatusNotUsedTest() {
        final String providerId = "providerId1";
        final Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
        final Page<GiftDto> page = giftRepository.findHistoryByProviderIdAndStatus(providerId, NOT_USED, pageable);
        assertThat(page.getNumberOfElements()).isEqualTo(5);
    }
}
