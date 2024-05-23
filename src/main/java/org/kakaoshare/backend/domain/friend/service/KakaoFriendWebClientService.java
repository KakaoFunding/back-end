package org.kakaoshare.backend.domain.friend.service;

import org.kakaoshare.backend.common.util.MultiValueMapConverter;
import org.kakaoshare.backend.domain.friend.dto.response.KakaoFriendListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoFriendWebClientService {
    private final String requestUrl;
    private final WebClient webClient;
    private final KakaoFriendRequestProvider requestProvider;

    public KakaoFriendWebClientService(@Value("${friend.request-url}") final String requestUrl,
                                       final WebClient webClient,
                                       final KakaoFriendRequestProvider requestProvider) {
        this.requestUrl = requestUrl;
        this.webClient = webClient;
        this.requestProvider = requestProvider;
    }

    public KakaoFriendListResponse getFriends(final String socialAccessToken) {
        final String requestUrlWithParams = getRequestUrlWithParams();
        return getFriends(socialAccessToken, requestUrlWithParams);
    }

    public KakaoFriendListResponse getFriends(final String socialAccessToken,
                                              final String requestUrlWithParams) {
        return webClient.get()
                .uri(requestUrlWithParams)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(socialAccessToken))
                .retrieve()
                .bodyToMono(KakaoFriendListResponse.class)
                .block();
    }

    private String getRequestUrlWithParams() {
        return UriComponentsBuilder
                .fromUriString(requestUrl)
                .queryParams(getQueryParams())
                .build()
                .toUriString();
    }

    private MultiValueMap<String, String> getQueryParams() {
        return MultiValueMapConverter.convert(requestProvider.createKakaoFriendListRequest());
    }
}
