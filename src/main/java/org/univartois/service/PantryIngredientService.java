package org.univartois.service;

import jakarta.transaction.Transactional;
import org.univartois.dto.request.PantryIngredientRequestDto;
import org.univartois.dto.response.IngredientResponseDto;
import org.univartois.dto.response.PantryIngredientResponseDto;
import org.univartois.entity.PantryIngredientEntity;
import org.univartois.mapper.PantryIngredientMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface PantryIngredientService {


    PantryIngredientResponseDto addPantryIngredient(UUID homeId,PantryIngredientRequestDto pantryIngredientRequestDto);

    List<PantryIngredientResponseDto> getPantryIngredientsByHomeId(UUID homeId);



    @Transactional
    PantryIngredientResponseDto getPantryIngredientByPantryIngredientIdAndHomeId(UUID homeId, UUID pantryIngredientId);

    PantryIngredientResponseDto updatePantryIngredient(UUID ingredientInPantryId, PantryIngredientRequestDto pantryIngredientRequestDto);

    void deletePantryIngredient(UUID ingredientInPantryId);

}
