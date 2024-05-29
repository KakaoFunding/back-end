package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.member.entity.Gender;
import org.kakaoshare.backend.domain.member.entity.Member;

import static org.kakaoshare.backend.domain.member.entity.Gender.FEMALE;
import static org.kakaoshare.backend.domain.member.entity.Gender.MALE;

public enum MemberFixture {
    KAKAO("카카오", 1L, MALE, "01012341234", "123"),
    KIM("김민우", 2L, MALE, "01011111111", "456"),
    HAN("한", 3L, FEMALE, "01022222222", "789");

    private final String name;
    private final Long memberId;
    private final Gender gender;
    private final String phoneNumber;
    private final String providerId;

    MemberFixture(final String name,
                  final Long memberId,
                  final Gender gender,
                  final String phoneNumber,
                  final String providerId) {
        this.name = name;
        this.memberId = memberId;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.providerId = providerId;
    }

    public Member 생성() {
        return Member.builder()
                .memberId(memberId)
                .name(name)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .providerId(providerId)
                .profileImageUrl("defaultProfileImageUrl")
                .build();
    }
}
