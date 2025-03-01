package org.univartois.dto.response;

import lombok.*;
import org.univartois.enums.IngredientUnit;

import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeResponseDto {
    private Creator creator;
    private String creatorName;
    private UUID idRecipe;
    private String recipeImageLink;
    private String recipeName;
    private int prepTimeMinutes;
    private String recipeVideoLink;
    private String recipeInstructions;
    private boolean recipePublic;
    private boolean recipeLunchBox;
    private int recipeNbCovers;
    private int cookTimeMinutes;
    private List<Ingredients> ingredients;



    public List<Ingredients> getIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        return ingredients;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Creator {
        private UUID id;
        private String username;
        private String imageUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Ingredients  {
        private UUID id;
        private String ingredientName;
        private Float quantityNeed;
        private Boolean vegan;
        private IngredientUnit unit;


    }


}