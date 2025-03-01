package org.univartois.dto.response;

import lombok.*;
import org.univartois.entity.IngredientEntity;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PantryIngredientResponseDto {


    private UUID idIngredientInPantry;
    private IngredientEntity ingredient;
    private float ingredientQuantity;
    private LocalDate ingredientExpirationDate;



}
