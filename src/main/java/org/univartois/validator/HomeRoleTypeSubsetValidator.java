package org.univartois.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.univartois.annotation.validation.HomeRoleTypeSubset;
import org.univartois.enums.HomeRoleType;

import java.util.Arrays;

public class HomeRoleTypeSubsetValidator implements ConstraintValidator<HomeRoleTypeSubset, HomeRoleType> {
    private HomeRoleType[] subset;


    public boolean isValid(HomeRoleType value, ConstraintValidatorContext context) {
        return value != null && Arrays.asList(this.subset).contains(value);
    }

    public void initialize(HomeRoleTypeSubset constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }
}

