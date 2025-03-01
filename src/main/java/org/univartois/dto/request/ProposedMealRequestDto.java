package org.univartois.dto.request;


import java.util.UUID;
import lombok.Data;

@Data
public class ProposedMealRequestDto {
    private UUID recipeId;
    private UUID mealId;
    private UUID proposerId;
}
