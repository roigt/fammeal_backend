package org.univartois.service;




import org.univartois.dto.request.IngredientRequestDto;
import org.univartois.dto.response.IngredientResponseDto;

import java.util.List;
import java.util.UUID;

public interface IngredientService {

    // Crée un nouvel ingrédient
    IngredientResponseDto createIngredient(IngredientRequestDto createIngredientRequestDto);

    // Récupère un ingrédient par son ID
    IngredientResponseDto getIngredientById(UUID ingredientId);

    // Récupère tous les ingrédients
    List<IngredientResponseDto> getAllIngredients();

    // Récupère les ingrédients végétaliens
    List<IngredientResponseDto> getVeganIngredients();

    // Récupère les ingrédients proches de leur expiration
    List<IngredientResponseDto> getIngredientsByExpiration(int days);

    IngredientResponseDto updateIngredient(UUID ingredientId, IngredientRequestDto ingredientRequestDto);

    void deleteIngredient(UUID ingredientId);


}
