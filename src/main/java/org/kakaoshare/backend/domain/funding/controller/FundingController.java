package org.kakaoshare.backend.domain.funding.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.funding.dto.FundingSliceResponse;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
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
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "4") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        FundingSliceResponse response = fundingService.getMyAllFundingProducts(providerId, pageRequest);
        return ResponseEntity.ok(response);
    }
}
