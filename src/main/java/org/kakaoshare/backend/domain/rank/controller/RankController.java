package org.kakaoshare.backend.domain.rank.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.rank.dto.RankResponse;
import org.kakaoshare.backend.domain.rank.service.RankService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ranking")
public class RankController {
    private final RankService rankService;
    @GetMapping
    public ResponseEntity<?> getTopRankedProducts(@PageableDefault(size = 20) Pageable pageable) {
        List<RankResponse> rankResponses = rankService.getTopRankedProducts(pageable);
        return ResponseEntity.ok(rankResponses);
    }
}
