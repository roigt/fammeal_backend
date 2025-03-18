package org.univartois.annotation.security;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
public @interface HomePermissionsAllowed {
    /**
     * List of allowed permissions
     */
    @Nonbinding String[] value() default {};

    /**
     * expression to retrieve homeId from method params
     */
    @Nonbinding String homeIdExpression() default "homeId";
}
