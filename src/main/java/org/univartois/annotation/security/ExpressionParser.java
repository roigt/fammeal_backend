package org.univartois.annotation.security;

import java.lang.reflect.Field;
import java.util.Map;

public class ExpressionParser {

    public static <T> T parseFromParams(String expression, Map<String, Object> params) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression is malformed");
        }
        String[] props = expression.split("\\.");
        Object param = params.get(props[0]);

        if (param == null) {
            throw new IllegalArgumentException(String.format("Method params do not contain key: '%s'", props[0]));
        }
        for (int i = 1; i < props.length; i++) {
            try {
                Field field = param.getClass().getDeclaredField(props[i]);
                field.setAccessible(true);
                param = field.get(param);
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                throw new IllegalArgumentException(String.format("Expression is malformed at '%s'", props[i]), exception);
            }
        }

        try {
            return (T) param;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Type mismatch: Cannot cast extracted value to expected type", e);
        }
    }
}
