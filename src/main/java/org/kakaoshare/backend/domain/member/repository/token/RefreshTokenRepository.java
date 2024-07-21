package org.kakaoshare.backend.domain.member.repository.token;

import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByValue(final String value);
}
