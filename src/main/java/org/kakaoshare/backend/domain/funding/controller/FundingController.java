package org.kakaoshare.backend.domain.funding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegistrationResponse;
import org.kakaoshare.backend.domain.funding.service.FundingService;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/funding")
public class FundingController {
    private final FundingService fundingService;

    @PostMapping("/{productId}")
    public ResponseEntity<?> registerFunding(@PathVariable Long productId, @LoggedInMember String providerId,
                                             @RequestBody
                                             RegisterRequest registerRequest) {
        RegistrationResponse response = fundingService.registerFundingItem(productId, providerId, registerRequest);
        return ResponseEntity.ok(response);
    }
}
