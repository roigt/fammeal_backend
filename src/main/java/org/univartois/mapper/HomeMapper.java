package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.entity.HomeEntity;

@Mapper(componentModel = "jakarta")
public interface HomeMapper {

    HomeEntity toEntity(CreateHomeRequestDto createHomeRequestDto);

    HomeResponseDto toHomeResponseDto(HomeEntity homeEntity, String role);
}
