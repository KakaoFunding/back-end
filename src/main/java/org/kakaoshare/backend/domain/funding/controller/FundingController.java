package org.kakaoshare.backend.domain.funding.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.common.vo.Date;
import org.kakaoshare.backend.domain.funding.dto.FundingSliceResponse;
import org.kakaoshare.backend.domain.funding.dto.ProgressResponse;
import org.kakaoshare.backend.domain.funding.dto.RegisterRequest;
import org.kakaoshare.backend.domain.funding.dto.RegisterResponse;
import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingPreviewRequest;
import org.kakaoshare.backend.domain.funding.dto.preview.response.FundingPreviewResponse;
import org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus;
import org.kakaoshare.backend.domain.funding.service.FundingService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FundingController {
    private static final int DEFAULT_HISTORY_SIZE = 10;
    private static final String DEFAULT_HISTORY_SORT_VALUE = "createdAt";

    private final FundingService fundingService;

    @GetMapping("/funding")
    public ResponseEntity<?> lookUp(@LoggedInMember final String providerId,
                                    @ModelAttribute final Date date,
                                    @EnumValue(enumClass = FundingDetailStatus.class, ignoreCase = true, nullable = true) final String status,
                                    @PageableDefault(size = DEFAULT_HISTORY_SIZE, sort = DEFAULT_HISTORY_SORT_VALUE, direction = Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity.ok(
                fundingService.lookUp(providerId, date, status, pageable)
        );
    }

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

    @PostMapping("/funding/preview")
    public ResponseEntity<?> preview(@RequestBody final FundingPreviewRequest fundingPreviewRequest) {
        final FundingPreviewResponse fundingPreviewResponse = fundingService.preview(fundingPreviewRequest);
        return ResponseEntity.ok(fundingPreviewResponse);
    }
}
