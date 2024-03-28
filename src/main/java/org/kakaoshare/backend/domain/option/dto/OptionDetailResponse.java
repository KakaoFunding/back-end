package org.kakaoshare.backend.domain.option.dto;

import lombok.Getter;

@Getter
public record OptionDetailResponse(Long id, String photo, String name) {
    public OptionDetailResponse {
    }
}