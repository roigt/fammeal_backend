package org.univartois.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealResponseDto {

    private UUID idMeal;
    private LocalDate mealDate;
    private boolean mealLunch;
    private UUID idRecipe;
}