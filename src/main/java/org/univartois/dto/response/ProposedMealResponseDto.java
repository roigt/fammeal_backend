package org.univartois.dto.response;



import java.time.LocalDate;
import java.util.UUID;

import lombok.*;


@Data
public class ProposedMealResponseDto {
    private UUID recipeId;
    private UUID mealId;
    private UUID proposerId;
    private RecipeDto recipe;
    private Boolean lunch;
    private LocalDate date;




    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class RecipeDto {
        private UUID idRecipe;
        private String name;
        private String imageUrl;
    }

}



