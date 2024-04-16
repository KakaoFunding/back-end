package org.kakaoshare.backend.domain.gift.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

public class GiftServiceTest {

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
    @DisplayName("선물 상세 정보 조회 - 선물이 존재할 때")
    void getGiftDetail_ReturnDetail() {
        Long giftId = 1L;
        GiftDetailResponse expectedResponse = GiftDetailResponse.builder()
                .giftId(giftId)
                .message("Happy Birthday")
                .messagePhoto("url_to_photo")
                .expiredAt(null)
                .createdAt(null)
                .status(null)
                .price(100L)
                .giftThumbnail("url_to_thumbnail")
                .build();

        when(giftRepository.findGiftDetailById(giftId)).thenReturn(expectedResponse);

        GiftDetailResponse actualResponse = giftService.getGiftDetail(giftId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(giftRepository).findGiftDetailById(giftId);
    }

    @Test
    @DisplayName("선물 상세 정보 조회 - 선물이 존재하지 않을 때 예외 발생")
    void getGiftDetail_ThrowException() {
        Long giftId = 1L;
        when(giftRepository.findGiftDetailById(giftId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> giftService.getGiftDetail(giftId));
    }

    @Test
    @DisplayName("선물 설명 정보 조회 - 선물이 존재할 때")
    void getGiftDescription_ReturnDescription() {
        Long giftId = 1L;
        GiftDescriptionResponse expectedResponse = GiftDescriptionResponse.builder()
                .productId(giftId)
                .name("Gift Name")
                .price(200L)
                .type("Type")
                .productName("Product Name")
                .brandName("Brand Name")
                .origin("Origin")
                .manufacturer("Manufacturer")
                .tel("Telephone")
                .deliverDescription("Delivery Info")
                .billingNotice("Billing Info")
                .caution("Caution Info")
                .giftThumbnail("url_to_thumbnail")
                .build();

        when(giftRepository.findGiftDescriptionById(giftId)).thenReturn(expectedResponse);

        GiftDescriptionResponse actualResponse = giftService.getGiftDescription(giftId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(giftRepository).findGiftDescriptionById(giftId);
    }

    @Test
    @DisplayName("선물 설명 정보 조회 - 선물이 존재하지 않을 때 예외 발생")
    void getGiftDescription_ThrowException() {
        Long giftId = 1L;
        when(giftRepository.findGiftDescriptionById(giftId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> giftService.getGiftDescription(giftId));
    }@Test
    @DisplayName("선물함 조회 테스트")
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

        when(memberRepository.findMemberByProviderId(providerId)).thenReturn(Optional.of(mockMember));
        when(giftRepository.findGiftsByMemberIdAndStatus(eq(mockMember.getMemberId()), eq(GiftStatus.NOT_USED), eq(pageable))).thenReturn(mockGiftResponses);

        Page<GiftResponse> result = giftService.getMyGiftBox(providerId, pageable, status);

        assertNotNull(result);
        assertEquals(1, result.getContent().size()); // 예상되는 결과 크기를 확인
    }
}
