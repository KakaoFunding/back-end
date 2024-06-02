package org.kakaoshare.backend.domain.gift.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.request.FundingGiftHistoryRequest;
import org.kakaoshare.backend.domain.gift.service.FundingGiftService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/funding/gift")
@RequiredArgsConstructor
public class FundingGiftController {
    private static final int DEFAULT_HISTORY_SIZE = 20;
    private static final String DEFAULT_HISTORY_SORT_VALUE = "createdAt";

    private final FundingGiftService fundingGiftService;

    @GetMapping
    public ResponseEntity<?> lookUp(@LoggedInMember final String providerId,
                                    @ModelAttribute final FundingGiftHistoryRequest fundingGiftHistoryRequest,
                                    @PageableDefault(size = DEFAULT_HISTORY_SIZE, sort = DEFAULT_HISTORY_SORT_VALUE, direction = Sort.Direction.DESC) final Pageable pageable) {
        return ResponseEntity.ok(
                fundingGiftService.lookUp(providerId, fundingGiftHistoryRequest, pageable)
        );
    }
}
