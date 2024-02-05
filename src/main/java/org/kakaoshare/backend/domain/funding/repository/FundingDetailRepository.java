package org.kakaoshare.backend.domain.funding.repository;

import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FundingDetailRepository extends JpaRepository<FundingDetail, Long> {
}
