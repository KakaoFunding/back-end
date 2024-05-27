package org.kakaoshare.backend.domain.member.entity;

import java.util.Arrays;

public enum Gender {
    MALE, FEMALE,ALL;

    public static Gender from(final String gender) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(gender.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }
    public static Gender fromTargetType(String gender) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(gender))
                .findFirst()
                .orElse(ALL);
    }
}
