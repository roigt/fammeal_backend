package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univartois.dto.response.HomeRoleTypeResponseDto;
import org.univartois.enums.HomeRoleType;

@Mapper(componentModel = "jakarta")
public interface HomeRoleTypeMapper {

    @Mapping(source = "value", target = "value")
    @Mapping(source = ".", target = "name")
    HomeRoleTypeResponseDto toHomeRoleTypeResponseDto(HomeRoleType homeRoleType);
}
