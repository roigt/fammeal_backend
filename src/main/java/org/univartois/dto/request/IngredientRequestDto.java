package org.univartois.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.univartois.enums.IngredientUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IngredientRequestDto {


    private IngredientUnit idUnit;

    @NotBlank(message = "Le nom de l'ingrédient ne doit pas être vide")
    private String ingredientName;

    private boolean ingredientIsVegan;

    @Positive(message = "Le nombre de jours avant expiration doit être positif")
    private int nbDayBeforeExpiration;
}
