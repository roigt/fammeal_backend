package org.univartois.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.univartois.enums.HomeRoleType;
import org.univartois.validator.HomeRoleTypeSubsetValidator;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {HomeRoleTypeSubsetValidator.class}
)
public @interface HomeRoleTypeSubset {
    HomeRoleType[] anyOf();

    String message() default "must be any of {anyOf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

