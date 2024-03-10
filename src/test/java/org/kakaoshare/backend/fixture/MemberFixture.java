package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.member.entity.Gender;
import org.kakaoshare.backend.domain.member.entity.Member;

import static org.kakaoshare.backend.domain.member.entity.Gender.MALE;

public enum MemberFixture {
    KAKAO("카카오", MALE, "01012341234", "123");

    private final String name;
    private final Gender gender;
    private final String phoneNumber;
    private final String providerId;

    MemberFixture(final String name,
                  final Gender gender,
                  final String phoneNumber,
                  final String providerId) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.providerId = providerId;
    }

    public Member 생성() {
        return Member.builder()
                .name(name)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .providerId(providerId)
                .build();
    }
}
