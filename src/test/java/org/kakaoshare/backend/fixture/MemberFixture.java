package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.member.entity.Member;

public enum MemberFixture {
    KAKAO("카카오", "123");

    private final String name;
    private final String providerId;

    MemberFixture(final String name, final String providerId) {
        this.name = name;
        this.providerId = providerId;
    }

    public Member 생성() {
        return Member.builder()
                .name(name)
                .providerId(providerId)
                .build();
    }
}
