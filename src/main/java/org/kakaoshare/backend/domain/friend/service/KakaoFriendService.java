package org.kakaoshare.backend.domain.friend.service;

import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.friend.dto.response.KakaoFriendListResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoFriendService {
    private final KakaoFriendWebClientService webClientService;

    public boolean isFriend(final String socialAccessToken,
                            final String receiverProviderId) {
        final KakaoFriendListResponse friends = webClientService.getFriends(socialAccessToken);
        final boolean existReceiverProviderId = existReceiverProviderId(receiverProviderId, friends);
        if (existReceiverProviderId) {
            return true;
        }
        return isFriend(socialAccessToken, receiverProviderId, friends.after_url());
    }

    private boolean isFriend(final String socialAccessToken,
                             final String receiverProviderId,
                             final String requestUrl) {
        String requestUrlWithParams = requestUrl;
        while (!StringUtils.isNullOrEmpty(requestUrlWithParams)) {
            final KakaoFriendListResponse friends = webClientService.getFriends(socialAccessToken, requestUrlWithParams);
            final boolean existReceiverProviderId = existReceiverProviderId(receiverProviderId, friends);
            if (existReceiverProviderId) {
                return true;
            }

            requestUrlWithParams = friends.after_url();
        }

        return false;
    }

    private boolean existReceiverProviderId(final String receiverProviderId,
                                            final KakaoFriendListResponse friends) {
        return Arrays.stream(friends.elements())
                .anyMatch(friend -> String.valueOf(friend.id()).equals(receiverProviderId));
    }
}
