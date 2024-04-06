package org.kakaoshare.backend.domain.option.dto;

import org.kakaoshare.backend.domain.option.entity.OptionDetail;

public record OptionSummaryResponse(String optionName, String optionDetailName) {
    public static OptionSummaryResponse from(final OptionDetail optionDetail) {
        return new OptionSummaryResponse(optionDetail.getOption().getName(), optionDetail.getName());
    }
}
