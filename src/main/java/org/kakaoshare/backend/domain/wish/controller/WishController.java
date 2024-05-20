package org.kakaoshare.backend.domain.wish.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.FriendsWishRequest;
import org.kakaoshare.backend.domain.wish.dto.MyWishDetail;
import org.kakaoshare.backend.domain.wish.service.WishService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
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
    private final WishService wishService;
    
    @GetMapping("/me")
    public ResponseEntity<?> getWishList(@LoggedInMember String providerId) {
        List<MyWishDetail> wishList = wishService.getMembersWishList(providerId);
        return ResponseEntity.ok(wishList);
    }
    
    @PostMapping("/{wishId}/change-type")
    public ResponseEntity<?> changeWishType(@LoggedInMember String providerId,
                                            @PathVariable(name = "wishId") Long wishId) {
        wishService.changeWishType(providerId, wishId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsWishList(@LoggedInMember String providerId,
                                                @RequestBody FriendsWishRequest friendsWishRequest) {
        List<FriendWishDetail> membersWishList = wishService.getFriendsWishList(providerId,friendsWishRequest);
        return ResponseEntity.ok(membersWishList);
    }
}
