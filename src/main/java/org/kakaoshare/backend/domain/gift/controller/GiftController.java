package org.kakaoshare.backend.domain.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.service.GiftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
}
