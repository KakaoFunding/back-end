package org.kakaoshare.backend.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
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
        final UserDetails expect = MemberDetails.from(member);
        doReturn(Optional.of(expect))
                .when(memberRepository)
                .findDetailsByProviderId(expect.getUsername());

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
                .findDetailsByProviderId(providerId);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(providerId))
                .isInstanceOf(MemberException.class);
    }
}