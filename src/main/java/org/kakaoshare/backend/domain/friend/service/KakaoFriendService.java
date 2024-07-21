package org.kakaoshare.backend.domain.friend.service;

import com.querydsl.core.util.StringUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.domain.friend.dto.response.KakaoFriendListResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoFriendListDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoFriendService {
    private static final String FRIENDS_LIST_SERVICE_URL = "https://kapi.kakao.com/v1/api/talk/friends";
    private final WebClient webClient;
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

    public List<KakaoFriendListDto> getFriendsList(String accessToken) {
        return webClient.get()
                .uri(FRIENDS_LIST_SERVICE_URL)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> (List<Map<String, Object>>) response.get("elements"))
                .map(elements -> elements.stream()
                        .map(element -> KakaoFriendListDto.builder()
                                .id(element.get("id").toString())
                                .uuid(element.get("uuid").toString())
                                .favorite((Boolean) element.get("favorite"))
                                .profileNickname(element.get("profile_nickname").toString())
                                .profileThumbnailImage(element.get("profile_thumbnail_image").toString())
                                .build())
                        .collect(Collectors.toList()))
                .block();
    }
}
