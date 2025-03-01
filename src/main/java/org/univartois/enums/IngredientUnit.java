package org.univartois.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IngredientUnit {
    GRAM("g", true),
    KILOGRAM("kg", true),
    LITER("L", true),
    MILLILITER("mL", true),
    UNIT("unité", false);

    private final String unitName;
    private final boolean unitFloatable;

    @JsonValue
    public String toJson() {
        return this.name();
    }
    public static IngredientUnit fromUnitName(String unitName) {
        for (IngredientUnit unit : IngredientUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unité inconnue : " + unitName);
    }

    @Override
    public String toString() {
        return this.unitName+" , "+this.unitFloatable;
    }
}

