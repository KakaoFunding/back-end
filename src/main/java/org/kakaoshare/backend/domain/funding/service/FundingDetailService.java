package org.kakaoshare.backend.domain.funding.service;

import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.friend.service.KakaoFriendService;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.ContributedFundingHistoryRequest;
import org.kakaoshare.backend.domain.funding.dto.inquiry.response.ContributedFundingHistoryResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.response.FundingContributorResponse;
import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoFriendListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FundingDetailService {
    private final FundingDetailRepository fundingDetailRepository;
    private final KakaoFriendService kakaoFriendService;

    public PageResponse<?> lookUp(final String providerId,
                                  final ContributedFundingHistoryRequest contributedFundingHistoryRequest,
                                  final Pageable pageable) {
        final String status = contributedFundingHistoryRequest.getStatus();
        final FundingHistoryDate date = contributedFundingHistoryRequest.toDate();
        final Page<?> page = getFundingDetailHistoryDto(providerId, date, status, pageable)
                .map(historyDto -> ContributedFundingHistoryResponse.of(historyDto, providerId));
        return PageResponse.from(page);
    }

    private Page<ContributedFundingHistoryDto> getFundingDetailHistoryDto(final String providerId, final FundingHistoryDate date, final String status, final Pageable pageable) {
        if (StringUtils.isNullOrEmpty(status)) {
            return fundingDetailRepository.findHistoryByConditionWithoutStatus(providerId, date, pageable);
        }

        return fundingDetailRepository.findHistoryByCondition(providerId, date, status, pageable);
    }

    public PageResponse<?> getTopContributors(Long fundingId, Pageable pageable, String accessToken) {
        Page<FundingDetail> fundingDetails = fundingDetailRepository.findTopContributorsByFundingId(fundingId, pageable);

        List<KakaoFriendListDto> friendsList = kakaoFriendService.getFriendsList(accessToken);

        List<FundingContributorResponse> responses = fundingDetails.stream()
                .map(detail -> {
                    KakaoFriendListDto friendProfile = findFriendProfile(detail.getMember().getProviderId(), friendsList);
                    return FundingContributorResponse.of(
                            friendProfile.getProfileThumbnailImage(),
                            friendProfile.getProfileNickname(),
                            detail.getAmount(),
                            calculateContributionPercentage(detail)
                    );
                }).toList();

        Page<FundingContributorResponse> contributorResponses = new PageImpl<>(responses, pageable, fundingDetails.getTotalElements());
        return PageResponse.from(contributorResponses);
    }

    private KakaoFriendListDto findFriendProfile(String providerId, List<KakaoFriendListDto> friendsList) {
        return friendsList.stream()
                .filter(friend -> friend.getId().equals(providerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching friend found for providerId: " + providerId));
    }



    private double calculateContributionPercentage(FundingDetail detail) {
        return 100.0 * detail.getAmount() / detail.getFunding().getGoalAmount();
    }
}
