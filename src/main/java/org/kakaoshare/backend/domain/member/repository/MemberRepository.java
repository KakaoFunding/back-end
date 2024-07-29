package org.kakaoshare.backend.domain.member.repository;

import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.query.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findMemberByProviderId(String providerId);
    @Query("SELECT m FROM Member m WHERE m.providerId IN :providerIds")
    List<Member> findByProviderIds(@Param("providerIds") List<String> providerIds);
}
