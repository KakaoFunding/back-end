package org.kakaoshare.backend.domain.member.repository;

import java.util.List;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.query.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findMemberByProviderId(String providerId);
    @Query("SELECT m FROM Member m WHERE m.providerId IN :providerIds")
    List<Member> findByProviderIds(List<String> providerIds);
}
