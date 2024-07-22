package org.kakaoshare.backend.domain.funding.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.funding.dto.rank.response.TopContributorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RepositoryTest
class FundingDetailRepositoryTest {
    @Autowired
    FundingDetailRepository fundingDetailRepository;

    @Test
    @DisplayName("최대 기여자 조회")
    public void findTopContributorsByFundingId() throws Exception {
        final Long fundingId = 1L;
        final Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "rate"));
        final Page<TopContributorResponse> page = fundingDetailRepository.findTopContributorsByFundingId(fundingId, pageable);
        final List<TopContributorResponse> content = page.getContent();
        Assertions.assertThat(content.get(0).rate()).isEqualTo(30.0);
        Assertions.assertThat(content.get(1).rate()).isEqualTo(20.0);
        Assertions.assertThat(content.get(2).rate()).isEqualTo(10.0);
    }
}