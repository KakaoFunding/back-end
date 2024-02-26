package org.kakaoshare.backend.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.jwt.service.UserDetailsServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @BeforeEach
    void setUp() {
        member = KAKAO.생성();
    }

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;  // @InjectionMocks는 인터페이스 Mocking을 지원하지 않아 구현체로 설정

    private Member member;

    @Test
    @DisplayName("이메일을 통해 알맞는 UserDetails 객체 조회")
    void loadByUsername() throws Exception {
        final String providerId = member.getProviderId();
        doReturn(Optional.of(member))
                .when(memberRepository)
                .findByProviderId(providerId);

        final UserDetails expect = new MemberDetails(member);
        final UserDetails actual = userDetailsService.loadUserByUsername(providerId);
        assertThat(expect)
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }

    @Test
    @DisplayName("유효하지 않은 이메일인 경우 예외 발생")
    public void loadByUsernameInvalidEmail() throws Exception {
        final String providerId = member.getProviderId();
        doReturn(Optional.empty())
                .when(memberRepository)
                .findByProviderId(providerId);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(providerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 이메일 입니다.");
    }
}