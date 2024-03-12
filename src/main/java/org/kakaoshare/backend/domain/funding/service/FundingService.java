package org.kakaoshare.backend.domain.funding.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegistrationResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FundingService {
    private final FundingRepository fundingRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public RegistrationResponse registerFundingItem(Long productId, String providerId, RegisterRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
        Funding funding = fundingRepository.save(request.toEntity(member, product));

        return RegistrationResponse.from(funding);
    }

    public ProgressResponse getFundingProgress(Long fundingId, String providerId) {
        Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
        Funding funding = fundingRepository.findByIdAndMemberId(fundingId, member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid fundingId"));

        return ProgressResponse.from(funding);
    }
}
