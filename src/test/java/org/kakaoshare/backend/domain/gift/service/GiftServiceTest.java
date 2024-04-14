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

public class GiftServiceTest {
    @Mock
    private GiftRepository giftRepository;

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

        when(giftRepository.findGiftDetail(giftId)).thenReturn(expectedResponse);

        GiftDetailResponse actualResponse = giftService.getGiftDetail(giftId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(giftRepository).findGiftDetail(giftId);
    }

    @Test
    @DisplayName("선물 상세 정보 조회 - 선물이 존재하지 않을 때 예외 발생")
    void getGiftDetail_ThrowException() {
        Long giftId = 1L;
        when(giftRepository.findGiftDetail(giftId)).thenReturn(null);

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

        when(giftRepository.findGiftDescription(giftId)).thenReturn(expectedResponse);

        GiftDescriptionResponse actualResponse = giftService.getGiftDescription(giftId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(giftRepository).findGiftDescription(giftId);
    }

    @Test
    @DisplayName("선물 설명 정보 조회 - 선물이 존재하지 않을 때 예외 발생")
    void getGiftDescription_ThrowException() {
        Long giftId = 1L;
        when(giftRepository.findGiftDescription(giftId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> giftService.getGiftDescription(giftId));
    }
}