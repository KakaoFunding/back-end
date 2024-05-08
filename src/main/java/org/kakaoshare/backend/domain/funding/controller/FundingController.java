package org.kakaoshare.backend.domain.funding.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.funding.dto.FundingSliceResponse;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingPreviewRequest;
import org.kakaoshare.backend.domain.funding.dto.preview.response.FundingPreviewResponse;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
import org.kakaoshare.backend.domain.funding.exception.FundingErrorCode;
import org.kakaoshare.backend.domain.funding.exception.FundingException;
import org.kakaoshare.backend.domain.funding.service.FundingService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FundingController {
    private final FundingService fundingService;

    @PostMapping("/funding/{productId}")
    public ResponseEntity<?> registerFunding(@PathVariable Long productId, @LoggedInMember String providerId,
                                             @RequestBody
                                             RegisterRequest registerRequest) {
        RegisterResponse response = fundingService.registerFundingItem(productId, providerId, registerRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/funding/{fundingId}")
    public ResponseEntity<?> getFundingProgress(@PathVariable Long fundingId, @LoggedInMember String providerId) {
        ProgressResponse response = fundingService.getFundingProgress(fundingId, providerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/funding/products")
    public ResponseEntity<?> getMyAllFundingProducts(@LoggedInMember String providerId,
                                                     @EnumValue(enumClass = FundingStatus.class, ignoreCase = true, message = "존재하지 않는 상태입니다.")
                                                     @RequestParam(name = "status", required = false, defaultValue = "PROGRESS") String status,
                                                     @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "20") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        FundingStatus fundingStatus = FundingStatus.valueOf(status.toUpperCase());
        FundingSliceResponse response = fundingService.getMyFilteredFundingProducts(providerId, fundingStatus, pageRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/funding/preview")
    public ResponseEntity<?> preview(@RequestBody final FundingPreviewRequest fundingPreviewRequest) {
        final FundingPreviewResponse fundingPreviewResponse = fundingService.preview(fundingPreviewRequest);
        return ResponseEntity.ok(fundingPreviewResponse);
    }
}
