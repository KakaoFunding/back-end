package org.kakaoshare.backend.common.util;

import com.querydsl.core.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class EnumValidator implements ConstraintValidator<EnumValue, String> {
    private EnumValue enumValue;

    @Override
    public void initialize(final EnumValue constraintAnnotation) {
        this.enumValue = constraintAnnotation;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (context == null) {
            return false;
        }

        if (StringUtils.isNullOrEmpty(value) && enumValue.nullable()) {
            return true;
        }

        final List<ParamEnum> paramEnums = getParamEnums();
        return paramEnums.stream()
                .anyMatch(enumConstant -> convertible(value, enumConstant));
    }

    private List<ParamEnum> getParamEnums() {
        return Arrays.stream(enumValue.enumClass().getEnumConstants())
                .map(enumConstant -> (ParamEnum) enumConstant)
                .toList();
    }

    private boolean convertible(final String value, final ParamEnum paramEnum) {
        final String trimValue = value.trim();
        if (enumValue.ignoreCase()) {
            return trimValue.equalsIgnoreCase(paramEnum.getParamName());
        }

        return trimValue.equals(paramEnum.getParamName());
    }
}
