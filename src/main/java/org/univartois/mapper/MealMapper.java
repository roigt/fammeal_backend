package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univartois.dto.request.MealRequestDto;
import org.univartois.dto.response.MealResponseDto;
import org.univartois.entity.MealEntity;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface MealMapper {

    @Mapping(source = "mealDate", target = "mealDate")
    @Mapping(source = "mealLunch", target = "mealLunch")
    @Mapping(source = "idRecipe", target = "recipe.idRecipe")
    MealEntity toEntity(MealRequestDto mealRequestDto);

    @Mapping(source = "mealDate", target = "mealDate")
    @Mapping(source = "mealLunch", target = "mealLunch")
    @Mapping(source = "recipe.idRecipe", target = "idRecipe")
    @Mapping(source ="idMeal" , target="idMeal")
    MealResponseDto toResponseDto(MealEntity mealEntity);

    List<MealResponseDto> toResponseDtoList(List<MealEntity> mealEntities);
}
