package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.member.entity.Member;

public record PaymentSuccessReceiver(boolean self, String name, String photoUrl) {
    public static PaymentSuccessReceiver of(final Member member, final String providerId) {
        return new PaymentSuccessReceiver(member.equalsProviderId(providerId), member.getName(), member.getProfileImageUrl());
    }
}
