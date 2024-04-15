package org.kakaoshare.backend.domain.gift.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.service.GiftService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GiftController {
    private final GiftService giftService;

    @GetMapping("/giftBox/giftInfo/{giftId}")
    public ResponseEntity<GiftDetailResponse> readGiftInfo(@PathVariable Long giftId) {
        GiftDetailResponse detailResponse = giftService.getGiftDetail(giftId);
        return ResponseEntity.ok(detailResponse);
    }

    @GetMapping("/giftBox/detail/{giftId}")
    public ResponseEntity<GiftDescriptionResponse> readGiftDetail(@PathVariable Long giftId) {
        GiftDescriptionResponse descriptionResponse = giftService.getGiftDescription(giftId);
        return ResponseEntity.ok(descriptionResponse);
    }
    @GetMapping("/giftBox")
    public ResponseEntity<?> getGiftBox(@LoggedInMember final String providerId,
                                        @PageableDefault(size = 20) Pageable pageable,
                                        @RequestParam(name = "status", required = false, defaultValue = "NOT_USED") GiftStatus status) {
        Page<GiftResponse> giftSliceResponse = giftService.getMyGiftBox(providerId, pageable, status);
        return ResponseEntity.ok(giftSliceResponse);
    }


}
