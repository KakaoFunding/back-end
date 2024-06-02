package org.kakaoshare.backend.domain.funding.entity;

import lombok.Getter;
import org.kakaoshare.backend.common.util.ParamEnum;

@Getter
public enum FundingDetailStatus implements ParamEnum {
    PROGRESS("진행 중"),
    COMPLETE("완료"),
    CANCEL_REFUND("취소/환불");

    private final String description;

    FundingDetailStatus(final String description) {
        this.description = description;
    }

    public boolean canceled() {
        return this.equals(CANCEL_REFUND);
    }

    @Override
    public String getParamName() {
        return name();
    }
}
