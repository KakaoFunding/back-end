package org.kakaoshare.backend.domain.gift.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GiftService {
    private final GiftRepository giftRepository;

    public GiftDetailResponse getGiftDetail(Long giftId) {
        GiftDetailResponse giftDetailResponse = giftRepository.findGiftDetail(giftId);
        if (giftDetailResponse == null) {
            throw new EntityNotFoundException("Gift not found with id: " + giftId);
        }
        return giftDetailResponse;
    }

    public GiftDescriptionResponse getGiftDescription(Long giftId){
        GiftDescriptionResponse giftDescriptionResponse = giftRepository.findGiftDescription(giftId);
        if (giftDescriptionResponse == null){
            throw  new EntityNotFoundException("Gift not found with id: " + giftId);
        }
        return giftDescriptionResponse;
    }
}
