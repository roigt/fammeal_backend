package org.univartois.service;

import jakarta.transaction.Transactional;
import org.univartois.dto.request.RecipeRequestDto;
import org.univartois.dto.response.RecipeResponseDto;

import java.util.List;
import java.util.UUID;

public interface RecipeService {

    @Transactional
    RecipeResponseDto createRecipe(String imageUrlRecipe,RecipeRequestDto createRecipeRequestDto);

    RecipeResponseDto getRecipeById(UUID recipeId);

    List<RecipeResponseDto> getAllRecipes();

    List<RecipeResponseDto> getAllPublicRecipes();


    @Transactional
    RecipeResponseDto updateRecipe(UUID recipeId, RecipeRequestDto recipeRequestDto,String imageUrl);

    @Transactional
    void deleteRecipe(UUID recipeId);

    List<RecipeResponseDto> searchRecipes(List<String> keywords, List<String> ingredientIds, Boolean vegetarian, Integer covers, Boolean lunchBox);

    @Transactional
    String uploadRecipeImage(byte[] image);
}
