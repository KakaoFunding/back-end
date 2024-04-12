package org.kakaoshare.backend.domain.gift.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
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

    public Page<GiftResponse> getMyGiftBox(String providerId, Pageable pageable, GiftStatus status) {
        Member member = findMemberByProviderId(providerId);
        return giftRepository.findGiftsByMemberIdAndStatus(member.getMemberId(), status,
                pageable);
    }

    private Member findMemberByProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
    }
}
