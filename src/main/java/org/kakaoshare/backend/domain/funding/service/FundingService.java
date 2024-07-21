package org.kakaoshare.backend.domain.funding.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;

import org.kakaoshare.backend.domain.friend.service.KakaoFriendService;
import org.kakaoshare.backend.domain.funding.dto.FriendFundingItemRequest;

import org.kakaoshare.backend.domain.funding.dto.FundingCheckRequest;
import org.kakaoshare.backend.domain.funding.dto.FundingResponse;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.FriendFundingInquiryRequest;
import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingPreviewRequest;
import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingProductDto;
import org.kakaoshare.backend.domain.funding.dto.preview.response.FundingPreviewResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
import org.kakaoshare.backend.domain.funding.exception.FundingErrorCode;
import org.kakaoshare.backend.domain.funding.exception.FundingException;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoFriendListDto;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.exception.ProductErrorCode;
import org.kakaoshare.backend.domain.product.exception.ProductException;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FundingService {
    private static final String PROGRESS_STATUS = "PROGRESS";
    private final FundingRepository fundingRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final KakaoFriendService kakaoFriendService;

    @Transactional
    public RegisterResponse registerFundingItem(Long productId, String providerId, RegisterRequest request) {
        Product product = findProductById(productId);
        Member member = findMemberByProviderId(providerId);

        validateGoalAmount(request.goalAmount(), product.getPrice());

        List<FundingResponse> existingFundings = fundingRepository.findFundingListByMemberId(member.getMemberId());
        for (FundingResponse funding : existingFundings) {
            if (funding.getStatus().equals(PROGRESS_STATUS)) {
                throw new FundingException(FundingErrorCode.ALREADY_REGISTERED);
            }
        }

        Funding newFunding = request.toEntity(member, product);
        newFunding = fundingRepository.save(newFunding);

        return RegisterResponse.from(newFunding);
    }

    public ProgressResponse getFundingItemProgress(Long fundingId, String providerId) {
        Member member = findMemberByProviderId(providerId);
        Funding funding = findByIdAndMemberId(fundingId, member.getMemberId());

        return getFundingProgress(funding.getFundingId(), member.getMemberId());
    }

    public ProgressResponse getMyFundingProgress(String providerId) {
        Member member = findMemberByProviderId(providerId);
        Funding funding = fundingRepository.findByMemberIdAndStatus(member.getMemberId(), FundingStatus.PROGRESS)
                .orElse(null);

        if (funding == null) {
            return new ProgressResponse();
        }
        return getFundingProgress(funding.getFundingId(), member.getMemberId());
    }


    public ProgressResponse getFriendFundingProgress(String providerId, FriendFundingInquiryRequest inquiryRequest) {
        Member self = findMemberByProviderId(providerId);
        Member friend = findMemberByProviderId(inquiryRequest.friendProviderId()); //todo 친구 검증 메소드 추가해야함
        return fundingRepository.findByMemberIdAndStatus(friend.getMemberId(), FundingStatus.PROGRESS)
                .map(funding -> getFundingProgress(funding.getFundingId(), friend.getMemberId()))
                .orElse(new ProgressResponse());
    }

    public PageResponse<?> getMyFilteredFundingProducts(String providerId, FundingStatus status,
                                                        Pageable pageable) {
        Member member = findMemberByProviderId(providerId);
        Page<FundingResponse> fundingResponses = fundingRepository.findFundingByMemberIdAndStatusWithPage(
                member.getMemberId(), status, pageable);

        return PageResponse.from(fundingResponses);
    }

    public List<FundingResponse> getFriendsActiveFundingItems(String providerId,
                                                              FriendFundingItemRequest friendFundingItemRequest) {
        List<String> providerIds = getFriendsProviderIds(friendFundingItemRequest);
        validateIsFriend(providerIds, friendFundingItemRequest);
        List<Member> members = memberRepository.findByProviderIds(providerIds);
        List<Long> memberIds = members.stream().map(Member::getMemberId).toList();

        List<Funding> fundingItems = fundingRepository.findActiveFundingItemsByMemberIds(memberIds,
                friendFundingItemRequest.getFundingStatus().getDescription());
        return fundingItems.stream().map(FundingResponse::from).toList();
    }

    public FundingPreviewResponse preview(final FundingPreviewRequest fundingPreviewRequest) {
        final Long fundingId = fundingPreviewRequest.fundingId();
        final FundingProductDto fundingProductDto = findFundingProductDtoById(fundingId);
        final Long productId = fundingProductDto.productId();
        final ProductDto productDto = findProductDtoByProductId(productId);
        return FundingPreviewResponse.of(productDto, fundingProductDto);
    }

    public ProgressResponse checkFundingItem(FundingCheckRequest fundingCheckRequest) {
        Member member = findMemberByProviderId(fundingCheckRequest.getProviderId());
        Funding funding = fundingRepository.findByMemberIdAndStatus(member.getMemberId(), FundingStatus.PROGRESS)
                .orElseThrow(() -> new FundingException(FundingErrorCode.NOT_FOUND));

        return getFundingProgress(funding.getFundingId(), member.getMemberId());
    }

    private Member findMemberByProviderId(String providerId) {
        return memberRepository.findMemberByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid providerId"));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
    }

    private FundingProductDto findFundingProductDtoById(final Long fundingId) {
        return fundingRepository.findAccumulateAmountAndProductIdById(fundingId)
                .orElseThrow(() -> new FundingException(FundingErrorCode.NOT_FOUND));
    }

    private ProductDto findProductDtoByProductId(final Long productId) {
        return productRepository.findProductDtoById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));
    }

    private List<String> getFriendsProviderIds(FriendFundingItemRequest friendFundingItemRequest) {
        List<KakaoFriendListDto> friendsList = kakaoFriendService.getFriendsList(
                friendFundingItemRequest.getAccessToken());
        return friendsList.stream()
                .map(KakaoFriendListDto::getId)
                .toList();
    }

    private void validateIsFriend(List<String> providerIds, FriendFundingItemRequest friendFundingItemRequest) {
        List<KakaoFriendListDto> friends = kakaoFriendService.getFriendsList(
                friendFundingItemRequest.getAccessToken());
        boolean isFriend = providerIds.stream().anyMatch(providerId ->
                friends.stream().anyMatch(friend -> friend.getId().equals(providerId)));
        if (!isFriend) {
            throw new MemberException(MemberErrorCode.NO_SUCH_RELATIONSHIP);
        }
    }

    private ProgressResponse getFundingProgress(final Long fundingId, final Long memberId) {
        Funding funding = fundingRepository.findByIdAndMemberId(fundingId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid fundingId"));

        return ProgressResponse.from(funding);
    }

    private Funding findByIdAndMemberId(Long fundingId, Long memberId) {
        return fundingRepository.findByIdAndMemberId(fundingId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid fundingId"));
    }

    private void validateGoalAmount(Long goalAmount, Long productPrice) {
        if (!goalAmount.equals(productPrice) && (goalAmount < 100 || goalAmount > productPrice - 100)) {
            throw new FundingException(FundingErrorCode.INVALID_GOAL_AMOUNT);
        }
        if (goalAmount > productPrice){
            throw new FundingException(FundingErrorCode.GOAL_AMOUNT_EXCEEDS_PRODUCT_PRICE);
        }
    }
}
