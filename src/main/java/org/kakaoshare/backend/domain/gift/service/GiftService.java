package org.kakaoshare.backend.domain.gift.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.response.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GiftService {
    private final GiftRepository giftRepository;
    private final MemberRepository memberRepository;

    public GiftDetailResponse getGiftDetail(Long giftId) {
        GiftDetailResponse giftDetailResponse = giftRepository.findGiftDetailById(giftId);
        if (giftDetailResponse == null) {
            throw new EntityNotFoundException("Gift not found with id: " + giftId);
        }
        return giftDetailResponse;
    }

    public GiftDescriptionResponse getGiftDescription(Long giftId) {
        GiftDescriptionResponse giftDescriptionResponse = giftRepository.findGiftDescriptionById(giftId);
        if (giftDescriptionResponse == null) {
            throw new EntityNotFoundException("Gift not found with id: " + giftId);
        }
        return giftDescriptionResponse;
    }

    public PageResponse<?> getMyGiftBox(final String providerId,
                                        final GiftStatus status,
                                        final Pageable pageable) {
        final Page<?> page = giftRepository.findHistoryByProviderIdAndStatus(providerId, status, pageable)
                .map(giftDto -> GiftResponse.of(giftDto, providerId));
        return PageResponse.from(page);
    }
}
