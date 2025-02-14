package org.univartois.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
public @interface HomePermissionsAllowed {
    String[] value();
    String homeId();
}
