package org.kakaoshare.backend.domain.funding.service;

import com.querydsl.core.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.friend.dto.response.Friend;
import org.kakaoshare.backend.domain.friend.service.KakaoFriendService;
import org.kakaoshare.backend.domain.funding.dto.inquiry.FundingContributorResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.ContributedFundingHistoryRequest;
import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoFriendListDto;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthWebClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final Page<?> page = getFundingDetailHistoryResponse(providerId, date, status, pageable);
        return PageResponse.from(page);
    }

    private Page<?> getFundingDetailHistoryResponse(final String providerId, final FundingHistoryDate date, final String status, final Pageable pageable) {
        if (StringUtils.isNullOrEmpty(status)) {
            return fundingDetailRepository.findHistoryByConditionWithoutStatus(providerId, date, pageable);
        }

        return fundingDetailRepository.findHistoryByCondition(providerId, date, status, pageable);
    }

    public Page<FundingContributorResponse> getTopContributors(Long fundingId, Pageable pageable, String accessToken) {
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

        return new PageImpl<>(responses, pageable, fundingDetails.getTotalElements());
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
