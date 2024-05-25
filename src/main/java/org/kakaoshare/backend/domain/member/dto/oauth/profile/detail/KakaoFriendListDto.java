package org.kakaoshare.backend.domain.member.dto.oauth.profile.detail;


import lombok.Builder;
import lombok.Data;
@Data
@Builder
public final class KakaoFriendListDto {
    private final String id;
    private final String uuid;
    private final boolean favorite;
    private final String profileNickname;
    private final String profileThumbnailImage;
}
