package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.entity.UserEntity;

@Mapper(componentModel = "jakarta")
public interface UserMapper {

    UserEntity toEntity(UserRegisterRequestDto userRegisterRequestDto);


    void updateEntity(@MappingTarget UserEntity user, UserRegisterRequestDto userRegisterRequestDto);
    
}
