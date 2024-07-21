package org.kakaoshare.backend.domain.option.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OptionResponse {
    private Long optionsId;
    private String name;
    private List<OptionDetailResponse> optionDetails;

}
