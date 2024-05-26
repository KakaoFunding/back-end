package org.kakaoshare.backend.domain.member.service.oauth;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoFriendListDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OAuthWebClientService {
    private static final String FRIENDS_LIST_SERVICE_URL = "https://kapi.kakao.com/v1/api/talk/friends";
    private final WebClient webClient;
    
    public Map<String, Object> getSocialProfile(final ClientRegistration registration,
                                                final String socialToken) {
        return webClient.get()
                .uri(getProfileRequestUri(registration))
                .headers(header -> header.setBearerAuth(socialToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    
    private String getProfileRequestUri(final ClientRegistration registration) {
        return registration.getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
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