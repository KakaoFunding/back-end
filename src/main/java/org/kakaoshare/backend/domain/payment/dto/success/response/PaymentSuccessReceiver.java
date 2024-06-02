package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.member.entity.Member;

public record PaymentSuccessReceiver(String name, String photoUrl) {
    public static PaymentSuccessReceiver from(final Member member) {
        return new PaymentSuccessReceiver(member.getName(), member.getProfileImageUrl());
    }
}
