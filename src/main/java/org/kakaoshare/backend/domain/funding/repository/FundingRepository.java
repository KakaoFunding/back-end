package org.kakaoshare.backend.domain.funding.repository;

import java.util.List;
import java.util.Optional;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.repository.query.FundingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FundingRepository extends JpaRepository<Funding, Long>, FundingRepositoryCustom {

}
