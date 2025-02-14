package org.univartois.mapper;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserAuthResponseDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.entity.UserEntity;

@Mapper(componentModel = "jakarta")
public interface UserMapper {

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    UserEntity toEntity(UserRegisterRequestDto userRegisterRequestDto);


    void updateEntity(@MappingTarget UserEntity user, UserRegisterRequestDto userRegisterRequestDto);

    UserAuthResponseDto toAuthResponseDto(UserEntity user, String accessToken);

    @Named("encodePassword")
    default String encodePassword(String password){
        return BcryptUtil.bcryptHash(password);
    }


    
}
