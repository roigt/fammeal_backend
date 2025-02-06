package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.entity.UserEntity;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(UserRegisterRequestDto userRegisterRequestDto);

}
