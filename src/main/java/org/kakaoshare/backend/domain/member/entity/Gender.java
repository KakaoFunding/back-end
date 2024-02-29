package org.kakaoshare.backend.domain.member.entity;

import java.util.Arrays;

public enum Gender {
    MALE, FEMALE;

    public static Gender from(final String gender) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(gender.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
