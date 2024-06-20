package org.kakaoshare.backend.domain.member.entity;

import com.querydsl.core.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.wish.entity.Wish;

import java.util.ArrayList;
import java.util.List;

import static org.kakaoshare.backend.domain.member.entity.Role.USER;


@Entity
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String profileImageUrl;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Role role = USER;

    @Builder.Default
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Wish> wishes=new ArrayList<>();

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    protected Member() {

    }

    @Builder
    public Member(final Long memberId, final Gender gender, final String name, final String phoneNumber, final String providerId, final String profileImageUrl) {
        this.memberId = memberId;
        this.gender = gender;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfileUrl(final String profileImageUrl) {
        if (StringUtils.isNullOrEmpty(profileImageUrl) || this.profileImageUrl.equals(profileImageUrl)) {
            return;
        }

        this.profileImageUrl = profileImageUrl;
    }

    public boolean equalsProviderId(final String providerId) {
        return this.providerId.equals(providerId);
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", gender=" + gender +
                ", username='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", providerId='" + providerId + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", role=" + role +
                '}';
    }

    public boolean isWishEmpty() {
        return wishes == null || wishes.isEmpty();
    }
}
