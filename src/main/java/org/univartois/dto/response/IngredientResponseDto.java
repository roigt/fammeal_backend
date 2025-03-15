package org.univartois.dto.response;

import lombok.*;
import org.univartois.enums.IngredientUnit;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Builder
public class IngredientResponseDto {

    private IngredientUnit idUnit;
    private String ingredientName;
    private boolean ingredientIsVegan;
    private int nbDayBeforeExpiration;
    private UUID idIngredient;
    private Map<Long, String> allergies;
}
