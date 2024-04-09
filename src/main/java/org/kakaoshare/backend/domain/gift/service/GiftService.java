package org.kakaoshare.backend.domain.gift.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftSliceResponse;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
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

    public GiftSliceResponse getMyGiftBox(String providerId, Pageable pageable) {
        Member member = findMemberByProviderId(providerId);
        Page<GiftResponse> giftResponses = giftRepository.findGiftsByMemberId(member.getMemberId(), pageable);

        PageResponse<GiftResponse> pageResponse = (PageResponse<GiftResponse>) PageResponse.from(giftResponses);

        return GiftSliceResponse.builder()
                .pageResponse(pageResponse)
                .build();
    }

    private Member findMemberByProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
    }
}
