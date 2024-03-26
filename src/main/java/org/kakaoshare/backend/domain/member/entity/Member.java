package org.kakaoshare.backend.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;

import static org.kakaoshare.backend.domain.member.entity.Role.USER;


@Entity
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String providerId;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = USER;

    protected Member() {

    }

    @Builder
    public Member(final Long memberId, final Gender gender, final String name, final String phoneNumber, final String providerId) {
        this.memberId = memberId;
        this.gender = gender;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", gender=" + gender +
                ", username='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", providerId='" + providerId + '\'' +
                ", role=" + role +
                '}';
    }
}
