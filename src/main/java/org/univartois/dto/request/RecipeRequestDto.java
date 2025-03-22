package org.univartois.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.univartois.utils.Constants;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {


    //    private String recipeImageLink;
    @Size(min = 2, max = 30, message = Constants.RECIPE_NAME_SIZE)
    private String recipeName;
    private int prepTimeMinutes;
    private String recipeVideoLink;
    private String recipeInstructions;
    private boolean recipePublic;
    private boolean recipeLunchBox;
    private int recipeNbCovers;
    private int cookTimeMinutes;
    private Map<UUID, Float> ingredients;

}