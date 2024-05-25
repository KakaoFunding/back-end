package org.kakaoshare.backend.domain.funding.entity;

import lombok.Getter;

@Getter
public enum FundingStatus {
    PROGRESS("진행 중"),
    BEFORE_PAYING_REMAINING("목표 금액 달성, 남은 금액 결제 전"),
    COMPLETE("완료"),
    EXPIRED("기간 만료"),
    CANCEL("취소");

    private final String description;

    FundingStatus(final String description) {
        this.description = description;
    }

    public boolean attributable() {
        return this.equals(PROGRESS) || this.equals(BEFORE_PAYING_REMAINING);
    }

    public boolean canceled() {
        return this.equals(CANCEL);
    }

    public boolean completed() {
        return this.equals(COMPLETE);
    }
}
