package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.entity.HomeEntity;

@Mapper(componentModel = "jakarta")
public interface HomeMapper {

    HomeResponseDto toHomeResponseDto(HomeEntity homeEntity);
}
