package org.kakaoshare.backend.domain.member.repository;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderId(final String providerId);
}
