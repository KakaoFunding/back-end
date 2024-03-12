package org.kakaoshare.backend.domain.funding.service;

import java.util.Optional;
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

        Optional<Funding> existingFunding = fundingRepository.findByIdAndMemberId(productId, member.getMemberId());
        if (existingFunding.isPresent() && "Active".equals(existingFunding.get().getStatus())) {
            throw new IllegalStateException("There is already an active funding for this product and user.");
        }

        Funding funding = request.toEntity(member, product);
        funding = fundingRepository.save(funding);

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
