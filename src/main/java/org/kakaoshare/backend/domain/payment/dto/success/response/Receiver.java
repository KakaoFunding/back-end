package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.member.entity.Member;

public record Receiver(String name, String photoUrl) {
    public static Receiver from(final Member member) {
        return new Receiver(member.getName(), null);    // TODO: 3/28/24 프로필 사진 URL이 없어 null로 대체
    }
}
