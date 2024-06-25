package org.kakaoshare.backend.domain.gift.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.GiftDto;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.response.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;
import static org.kakaoshare.backend.fixture.MemberFixture.KIM;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;

    @InjectMocks
    private GiftService giftService;

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
    }

    @ParameterizedTest
    @DisplayName("선물함 조회 테스트 (상태 필터링)")
    @EnumSource(value = GiftStatus.class, names = {"NOT_USED", "USING", "USED", "CANCEL_REFUND"})
    void getMyGiftBoxTest(final GiftStatus status) {
        final Pageable pageable = Pageable.unpaged();
        final Member receiver = KIM.생성();
        final Member sender = KAKAO.생성();

        final String receiverProviderId = receiver.getProviderId();
        final String senderProviderId = sender.getProviderId();

        final Product cake = CAKE.생성();
        final GiftDto giftDto = new GiftDto(
                1L,
                LocalDateTime.now().plusDays(180L),
                LocalDateTime.now(),
                sender.getName(),
                senderProviderId,
                ProductDto.from(cake)
        );
        final List<GiftDto> giftDtos = List.of(giftDto);
        final Page<GiftDto> page = new PageImpl<>(giftDtos, pageable, giftDtos.size());
        when(giftRepository.findHistoryByProviderIdAndStatus(receiverProviderId, status, pageable)).thenReturn(page);
        final Page<?> giftResponses = page.map(dto -> GiftResponse.of(dto, receiverProviderId));

        final PageResponse<?> actual = giftService.getMyGiftBox(receiverProviderId, status, pageable);
        final PageResponse<?> expect = PageResponse.from(giftResponses);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expect);
    }
}
