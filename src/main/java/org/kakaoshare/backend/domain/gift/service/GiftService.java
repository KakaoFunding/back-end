package org.kakaoshare.backend.domain.gift.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
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

    public PageResponse<?> getMyGiftBox(String providerId, Pageable pageable, GiftStatus status) {
        Member member = findMemberByProviderId(providerId);
        Page<GiftResponse> gifts = giftRepository.findGiftsByMemberIdAndStatus(member.getMemberId(), status,
                pageable);
        return PageResponse.from(gifts);
    }

    private Member findMemberByProviderId(String providerId) {
        return memberRepository.findMemberByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }
}
