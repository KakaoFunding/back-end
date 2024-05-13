package org.kakaoshare.backend.domain.funding.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus;
import org.kakaoshare.backend.domain.funding.service.FundingDetailService;
import org.kakaoshare.backend.domain.funding.vo.FundingHistoryDate;
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
@RequestMapping("/api/v1/fundingDetail")
@RequiredArgsConstructor
public class FundingDetailController {
    private static final int DEFAULT_HISTORY_SIZE = 10;
    private static final String DEFAULT_HISTORY_SORT_VALUE = "createdAt";

    private final FundingDetailService fundingDetailService;

    @GetMapping
    public ResponseEntity<?> lookUp(@LoggedInMember final String providerId,
                                    @ModelAttribute final FundingHistoryDate date,
                                    @EnumValue(enumClass = FundingDetailStatus.class, ignoreCase = true, nullable = true) final String status,
                                    @PageableDefault(size = DEFAULT_HISTORY_SIZE, sort = DEFAULT_HISTORY_SORT_VALUE, direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity.ok(
                fundingDetailService.lookUp(providerId, date, status, pageable)
        );
    }
}
