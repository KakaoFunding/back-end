package org.kakaoshare.backend.common.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumValue, String> {
    private EnumValue enumValue;

    @Override
    public void initialize(final EnumValue constraintAnnotation) {
        this.enumValue = enumValue;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        final Enum<?>[] enumConstants = enumValue.enumClass().getEnumConstants();
        if (enumConstants == null) {
            return false;
        }

        return Arrays.stream(enumConstants)
                .anyMatch(enumConstant -> convertible(value, enumConstant));
    }

    private boolean convertible(final String value, final Enum<?> enumConstant) {
        final String trimValue = value.trim();
        if (enumValue.ignoreCase()) {
            return trimValue.equalsIgnoreCase(enumConstant.name());
        }

        return trimValue.equals(enumConstant.name());
    }
}
