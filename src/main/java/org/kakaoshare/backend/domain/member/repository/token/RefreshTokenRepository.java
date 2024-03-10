package org.kakaoshare.backend.domain.member.repository.token;

import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
