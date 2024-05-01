package org.kakaoshare.backend.domain.funding.entity;

import lombok.Getter;

@Getter
public enum FundingDetailStatus {
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
}
