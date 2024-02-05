package org.kakaoshare.backend.domain.funding.repository;

import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FundingRepository extends JpaRepository<Funding, Long> {
}
