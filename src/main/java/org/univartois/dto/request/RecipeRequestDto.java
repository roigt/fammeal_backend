package org.univartois.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {


//    private String recipeImageLink;
    private String recipeName;
    private int prepTimeMinutes;
    private String recipeVideoLink;
    private String recipeInstructions;
    private boolean recipePublic;
    private boolean recipeLunchBox;
    private int recipeNbCovers;
    private int cookTimeMinutes;
    private Map<UUID, Float> ingredients ;

}