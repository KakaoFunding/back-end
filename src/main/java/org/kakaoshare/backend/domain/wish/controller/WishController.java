package org.kakaoshare.backend.domain.wish.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.wish.dto.WishDetail;
import org.kakaoshare.backend.domain.wish.service.WishService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/wishes")
@RestController
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;
    
    @GetMapping("/me")
    public ResponseEntity<?> getWishList(@LoggedInMember String providerId){
        List<WishDetail> wishList = wishService.getMembersWishList(providerId);
        return ResponseEntity.ok(wishList);
    }
    
}
