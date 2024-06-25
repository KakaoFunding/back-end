package org.kakaoshare.backend.domain.funding.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;

import org.kakaoshare.backend.domain.funding.dto.FundingCheckRequest;
import org.kakaoshare.backend.domain.funding.dto.inquiry.request.FriendFundingInquiryRequest;
import org.kakaoshare.backend.domain.funding.dto.FriendFundingItemRequest;
import org.kakaoshare.backend.domain.funding.dto.FundingResponse;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingPreviewRequest;
import org.kakaoshare.backend.domain.funding.dto.preview.response.FundingPreviewResponse;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
import org.kakaoshare.backend.domain.funding.service.FundingDetailService;
import org.kakaoshare.backend.domain.funding.service.FundingService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FundingController {
    private final FundingService fundingService;
    private final FundingDetailService fundingDetailService;
    private static final int FUNDING_DEFAULT_SIZE = 20;

    @PostMapping("/funding/{productId}")
    public ResponseEntity<?> registerFunding(@PathVariable("productId") Long productId,
                                             @LoggedInMember String providerId,
                                             @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = fundingService.registerFundingItem(productId, providerId, registerRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/funding/check")
    public ResponseEntity<?> checkFunding(@RequestBody FundingCheckRequest checkRequest){

        ProgressResponse response = fundingService.checkFundingItem(checkRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/funding/friendItem")
    public ResponseEntity<?> getFriendFundingProgress(@LoggedInMember String providerId, @RequestBody
    FriendFundingInquiryRequest inquiryRequest) {
        ProgressResponse response = fundingService.getFriendFundingProgress(providerId, inquiryRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/funding/myItem")
    public ResponseEntity<?> getMyFundingProgress(@LoggedInMember String providerId){
        ProgressResponse response = fundingService.getMyFundingProgress(providerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/funding/friends")
    public ResponseEntity<?> getFriendsActiveFundingItems(@LoggedInMember String providerId,
                                                          @RequestBody FriendFundingItemRequest fundingRequest) {
        List<FundingResponse> activeFundings = fundingService.getFriendsActiveFundingItems(providerId, fundingRequest);
        return ResponseEntity.ok(activeFundings);
    }

    @GetMapping("/members/funding/products")
    public ResponseEntity<?> getMyAllFundingProducts(@LoggedInMember String providerId,
                                                     @RequestParam(name = "status", required = false) FundingStatus status,
                                                     @PageableDefault(size = FUNDING_DEFAULT_SIZE) final Pageable pageable) {
        PageResponse<?> response = fundingService.getMyFilteredFundingProducts(providerId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/funding/preview")
    public ResponseEntity<?> preview(@RequestBody final FundingPreviewRequest fundingPreviewRequest) {
        final FundingPreviewResponse fundingPreviewResponse = fundingService.preview(fundingPreviewRequest);
        return ResponseEntity.ok(fundingPreviewResponse);
    }

    @GetMapping("/{fundingId}/contributors")
    public ResponseEntity<?> getTopContributors(@PathVariable Long fundingId,
                                                @PageableDefault(size = 5) Pageable pageable,
                                                @RequestHeader("Authorization") String accessToken) {

        accessToken = accessToken.substring("Bearer ".length());
        PageResponse<?> contributors = fundingDetailService.getTopContributors(fundingId, pageable,
                accessToken);
        return ResponseEntity.ok(contributors);
    }
}
