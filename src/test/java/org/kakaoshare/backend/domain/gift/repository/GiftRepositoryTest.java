package org.kakaoshare.backend.domain.gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.CANCEL_REFUND;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.NOT_USED;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.USED;
import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.USING;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

@RepositoryTest
public class GiftRepositoryTest {
    @Autowired
    private GiftRepository giftRepository;
    private Pageable pageable;

    static Stream<Arguments> statusData() {
        return Stream.of(
                Arguments.of(1L, USING),
                Arguments.of(1L, NOT_USED),
                Arguments.of(1L, USED),
                Arguments.of(1L, CANCEL_REFUND),
                Arguments.of(2L, USING),
                Arguments.of(2L, NOT_USED)
        );
    }

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
    }

    @ParameterizedTest
    @DisplayName("선물함 조회")
    @MethodSource("statusData")
    public void findGiftsByMemberIdAndStatusTest(Long memberId, GiftStatus status) {
        final Page<GiftResponse> response = giftRepository.findGiftsByMemberIdAndStatus(memberId, status, pageable);
        assertThat(response.hasContent()).isTrue();
    }

    @Test
    @DisplayName("특정 사용자의 선물함 조회 - 사용하지 않음")
    public void findGiftsByMemberId1AndStatusNotUsedTest() {
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt"));
        final Page<GiftResponse> response = giftRepository.findGiftsByMemberIdAndStatus(memberId, NOT_USED, pageable);
        assertThat(response.getNumberOfElements()).isEqualTo(5);
    }

}
