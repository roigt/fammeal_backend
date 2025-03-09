package org.univartois.dto.request;


import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class ProposedMealRequestDto {
    private UUID recipeId;
//    private UUID mealId;
//    private UUID proposerId;
    private LocalDate date;
    private Boolean lunch;

}
