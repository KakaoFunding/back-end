package org.kakaoshare.backend.domain.friend.dto.response;

public record Friend(Long id, String uuid, Boolean favorite, String profile_nickname, String profile_thumbnail_image) {
}
