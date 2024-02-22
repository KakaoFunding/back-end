package org.kakaoshare.backend.jwt.service;

import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    public UserDetailsServiceImpl(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(MemberDetails::new)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이메일 입니다."));
    }
}
