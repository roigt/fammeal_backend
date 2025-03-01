package org.univartois.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.univartois.dto.request.RecipeRequestDto;
import org.univartois.dto.response.RecipeResponseDto;

import org.univartois.entity.RecipeEntity;

import java.util.List;


@Mapper(componentModel = "jakarta")
public interface RecipeMapper {

    RecipeEntity toEntity(RecipeRequestDto recipeRequestDto);

    @Mapping(source="user.firstname", target="creatorName")
    @Mapping(source="user.id" , target="creator.id")
    @Mapping(source="user.username" , target="creator.username")
    @Mapping(source="user.profilePictureUrl" , target="creator.imageUrl")
    RecipeResponseDto toResponseDto(RecipeEntity recipeEntity);

    List<RecipeResponseDto> toResponseDtoList(List<RecipeEntity> recipeEntities);

}