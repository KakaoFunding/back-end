package org.kakaoshare.backend.domain.member.repository.query;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<UserDetails> findDetailsByProviderId(final String providerId);
}
