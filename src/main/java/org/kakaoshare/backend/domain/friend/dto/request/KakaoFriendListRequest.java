package org.kakaoshare.backend.domain.friend.dto.request;

public record KakaoFriendListRequest(Integer offset, Integer limit, String order, String friend_order) {
}
