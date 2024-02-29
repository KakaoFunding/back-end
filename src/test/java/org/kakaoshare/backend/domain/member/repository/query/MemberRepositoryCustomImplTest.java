package org.kakaoshare.backend.domain.member.repository.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.DatabaseCleanUp;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;

@EnableJpaAuditing
@SpringBootTest
class MemberRepositoryCustomImplTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    public void setUp() {
        databaseCleanUp.execute();
    }

    @Test
    @DisplayName("providerId로 Member 조회")
    public void findDetailsByProviderId() throws Exception {
        final Member member = KAKAO.생성();
        memberRepository.save(member);

        final UserDetails userDetails = memberRepository.findDetailsByProviderId(member.getProviderId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(MemberDetails.from(member)).usingRecursiveComparison()
                .isEqualTo(userDetails);
    }
}