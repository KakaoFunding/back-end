package org.kakaoshare.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.kakaoshare.backend.domain.member.exception.MemberErrorCode.NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return memberRepository.findDetailsByProviderId(username)
                .orElseThrow(() -> new MemberException(NOT_FOUND));
    }
}
