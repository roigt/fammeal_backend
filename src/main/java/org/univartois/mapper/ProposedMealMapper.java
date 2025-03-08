package org.univartois.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univartois.dto.request.ProposedMealRequestDto;
import org.univartois.dto.response.ProposedMealResponseDto;
import org.univartois.entity.MealEntity;
import org.univartois.entity.ProposedMealEntity;
import org.univartois.entity.RecipeEntity;
import org.univartois.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface ProposedMealMapper {

    @Mapping(source = "recipeId", target = "recipe.idRecipe")
    @Mapping(source="date", target ="meal.mealDate")
    @Mapping(source="lunch", target ="meal.mealLunch")
    ProposedMealEntity toEntity(ProposedMealRequestDto dto);

    @Mapping(source = "recipe.idRecipe", target = "recipeId")
    @Mapping(source ="recipe.recipeName", target ="recipe.name")
    @Mapping(source ="recipe.recipeImageLink", target ="recipe.imageUrl")
    @Mapping(source = "meal.idMeal", target = "mealId")
    @Mapping(source = "proposer.id", target = "proposerId")
    @Mapping(source="meal.mealDate", target ="date")
    @Mapping(source="meal.mealLunch", target="lunch")
    ProposedMealResponseDto toResponseDto(ProposedMealEntity entity);

    List<ProposedMealResponseDto> toResponseDtoList(List<ProposedMealEntity> entities);
}
