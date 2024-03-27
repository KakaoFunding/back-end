package org.kakaoshare.backend.domain.option.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionResponse {
    private String name;
    private String optionDetailName;
    private Long additionalPrice;
    private String photo;
}
