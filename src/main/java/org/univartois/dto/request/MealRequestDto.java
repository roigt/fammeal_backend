package org.univartois.dto.request;


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
public class MealRequestDto {

    private LocalDate mealDate;
    private boolean mealLunch;
    private UUID idRecipe;
}