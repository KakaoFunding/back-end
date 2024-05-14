package org.kakaoshare.backend.domain.friend.dto.response;

public record KakaoFriendListResponse(Friend[] elements, Integer total_count, String before_url, String after_url, Integer favorite_count) {
}
