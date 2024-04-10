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
    private final Long optionsId;
    private final String name;
    private List<OptionDetailResponse> optionDetails;

    public void setOptionDetails(List<OptionDetailResponse> optionDetails) {
        this.optionDetails = optionDetails;
    }
}
