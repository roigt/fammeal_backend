package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univartois.dto.request.IngredientRequestDto;
import org.univartois.dto.response.IngredientResponseDto;
import org.univartois.entity.IngredientEntity;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface IngredientMapper {


    IngredientEntity toEntity(IngredientRequestDto ingredientRequestDto);


    @Mapping(source="idIngredient", target="IngredientId")
    IngredientResponseDto toResponseDto(IngredientEntity ingredientEntity);

    List<IngredientResponseDto> toResponseDtoList(List<IngredientEntity> ingredientEntities);
}
