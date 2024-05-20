package org.kakaoshare.backend.domain.friend.service;

import org.kakaoshare.backend.domain.friend.dto.request.KakaoFriendListRequest;
import org.springframework.stereotype.Component;

@Component
public class KakaoFriendRequestProvider {
    private static final int OFFSET = 0;
    private static final int LIMIT = 100;
    private static final String ORDER = "asc";
    private static final String FRIEND_ORDER = "nickname";

    public KakaoFriendListRequest createKakaoFriendListRequest() {
        return new KakaoFriendListRequest(OFFSET, LIMIT, ORDER, FRIEND_ORDER);
    }
}
