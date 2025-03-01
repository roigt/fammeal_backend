package org.univartois.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PantryIngredientRequestDto {

    private UUID id_ingredient;
    private LocalDate ingredientExpirationDate;
    private int ingredientQuantity;


}
