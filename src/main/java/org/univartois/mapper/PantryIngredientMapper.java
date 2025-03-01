package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univartois.dto.request.PantryIngredientRequestDto;
import org.univartois.dto.response.PantryIngredientResponseDto;
import org.univartois.entity.PantryIngredientEntity;

import java.util.List;



@Mapper(componentModel = "jakarta")
public interface PantryIngredientMapper {





    @Mapping(source = "id_ingredient", target = "ingredient.idIngredient")
    @Mapping(source = "ingredientQuantity", target = "ingredientQuantity")
    @Mapping(source = "ingredientExpirationDate", target = "ingredientExpirationDate")
    PantryIngredientEntity toEntity(PantryIngredientRequestDto pantryIngredientRequestDto);



    @Mapping(source="idIngredientInPantry", target = "idIngredientInPantry")
    @Mapping(source = "ingredient", target = "ingredient")
    @Mapping(source = "ingredientQuantity", target = "ingredientQuantity")
    @Mapping(source = "ingredientExpirationDate", target = "ingredientExpirationDate")
    PantryIngredientResponseDto toResponseDto(PantryIngredientEntity pantryIngredientEntity);

    List<PantryIngredientResponseDto> toResponseDtoList(List<PantryIngredientEntity> pantryIngredientEntity);
}
