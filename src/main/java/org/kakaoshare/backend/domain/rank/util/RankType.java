package org.kakaoshare.backend.domain.rank.util;

public enum RankType {
    MANY_WISH("위시가 많은 순"),
    MANY_RECEIVE("많이 선물 받은 순");

    private final String description;

    RankType(String description) {
        this.description = description;
    }
}
