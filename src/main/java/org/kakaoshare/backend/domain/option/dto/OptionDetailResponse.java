package org.kakaoshare.backend.domain.option.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDetailResponse {
    private Long id;
    private String photo;
    private String name;

}