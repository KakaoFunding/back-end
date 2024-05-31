package org.kakaoshare.backend.common.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {
    Class<? extends Enum<? extends ParamEnum>> enumClass();
    String message() default "";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean ignoreCase() default false;
    boolean nullable() default false;
}
