package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univartois.dto.response.HomeRoleResponseDto;
import org.univartois.enums.HomeRoleType;

@Mapper(componentModel = "jakarta")
public interface HomeRoleMapper {

    @Mapping(source = ".", target = "name")
    @Mapping(source = "value", target = "value")
    HomeRoleResponseDto toHomeRoleResponseDto(HomeRoleType homeRole);
}
