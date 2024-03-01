package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.member.entity.Member;

public enum MemberFixture {
    KAKAO("kakao@kakao.com", "카카오");

    private final String email;
    private final String name;

    MemberFixture(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public Member 생성() {
        return Member.builder()
                .email(email)
                .name(name)
                .build();
    }
}
