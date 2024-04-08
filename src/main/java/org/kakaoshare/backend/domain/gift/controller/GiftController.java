package org.kakaoshare.backend.domain.gift.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.service.GiftService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GiftController {
    private final GiftService giftService;
}
