package org.kakaoshare.backend.domain.gift.entity;

public enum GiftStatus {
    NOT_USED("사용 안함"),
    USING("사용중"),
    USED("사용 완료");

    private final String description;

    GiftStatus(final String description) {
        this.description = description;
    }
}
