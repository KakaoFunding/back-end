package org.kakaoshare.backend.domain.gift.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private GiftService giftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMyGiftBoxTest() {
        String providerId = "providerId";
        Pageable pageable = Pageable.unpaged();
        Member mockMember = MemberFixture.KAKAO.생성();
        GiftStatus status = GiftStatus.NOT_USED;

        GiftResponse mockGiftResponse = GiftResponse.builder()
                .giftId(1L)
                .expiredAt(LocalDateTime.now().plusDays(30))
                .recipientName("수령인 이름")
                .productName("상품 이름")
                .productThumbnail("상품 썸네일 URL")
                .brandName("브랜드 이름")
                .build();
        List<GiftResponse> giftResponseList = List.of(mockGiftResponse);
        Page<GiftResponse> mockGiftResponses = new PageImpl<>(giftResponseList, pageable, giftResponseList.size());

        when(memberRepository.findByProviderId(providerId)).thenReturn(Optional.of(mockMember));
        when(giftRepository.findGiftsByMemberIdAndStatus(eq(mockMember.getMemberId()), eq(GiftStatus.NOT_USED), eq(pageable))).thenReturn(mockGiftResponses);

        Page<GiftResponse> result = giftService.getMyGiftBox(providerId, pageable, status);

        assertNotNull(result);
        assertEquals(1, result.getContent().size()); // 예상되는 결과 크기를 확인
    }
}
