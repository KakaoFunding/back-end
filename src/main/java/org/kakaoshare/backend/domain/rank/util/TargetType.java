package org.kakaoshare.backend.domain.rank.util;

public enum TargetType {
    ALL("전체"),
    FEMALE("여성"),
    MALE("남성");
    private final String description;

    TargetType(String description) {
        this.description = description;
    }
}
