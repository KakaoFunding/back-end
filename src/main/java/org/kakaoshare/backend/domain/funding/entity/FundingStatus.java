package org.kakaoshare.backend.domain.funding.entity;

import lombok.Getter;

@Getter
public enum FundingStatus {
    PROGRESS("진행 중"),
    COMPLETE("완료"),
    CANCEL("취소");

    private final String description;

    FundingStatus(final String description) {
        this.description = description;
    }

    public boolean canceled() {
        return this.equals(CANCEL);
    }

}
