package org.kakaoshare.backend.domain.gift.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.GiftSliceResponse;
import org.kakaoshare.backend.domain.gift.service.GiftService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GiftController {
    private final GiftService giftService;

    @GetMapping("giftBox")
    public ResponseEntity<?> getGiftBox(@LoggedInMember final String providerId,
                                        @PageableDefault(size = 20) Pageable pageable) {
        GiftSliceResponse giftSliceResponse = giftService.getMyGiftBox(providerId, pageable);
        return ResponseEntity.ok(giftSliceResponse);

    }
}
