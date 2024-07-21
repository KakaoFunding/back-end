package org.kakaoshare.backend.domain.wish.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.FriendsWishRequest;
import org.kakaoshare.backend.domain.wish.service.WishService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/wishes")
@RestController
@RequiredArgsConstructor
public class WishController {
    public static final int PAGE_DEFAULT_SIZE = 20;
    private final WishService wishService;
    
    @GetMapping("/me")
    public ResponseEntity<?> getWishList(@LoggedInMember String providerId,
                                         @PageableDefault(size = PAGE_DEFAULT_SIZE) Pageable pageable) {
        PageResponse<?> wishList = wishService.getMembersWishList(pageable, providerId);
        return ResponseEntity.ok(wishList);
    }
    
    @PostMapping("/{wishId}/change-type")
    public ResponseEntity<?> changeWishType(@LoggedInMember String providerId,
                                            @PathVariable(name = "wishId") Long wishId) {
        wishService.changeWishType(providerId, wishId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/friends")
    public ResponseEntity<?> getFriendsWishList(@LoggedInMember String providerId,
                                                @RequestBody FriendsWishRequest friendsWishRequest) {
        List<FriendWishDetail> membersWishList = wishService.getFriendsWishList(providerId,friendsWishRequest);
        return ResponseEntity.ok(membersWishList);
    }
}