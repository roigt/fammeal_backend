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
import org.univartois.enums.HomeRoleType;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta")
public interface UserMapper {

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    UserEntity toEntity(UserRegisterRequestDto userRegisterRequestDto);


    void updateEntity(@MappingTarget UserEntity user, UserRegisterRequestDto userRegisterRequestDto);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.lastname", target = "lastname")
    @Mapping(source = "user.vegetarian", target = "vegetarian")
    @Mapping(source = "user.verified", target = "verified")
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "roles", target = "roles")
    UserAuthResponseDto toAuthResponseDto(UserEntity user, String accessToken, Map<String, Set<String>> roles);


    @Named("encodePassword")
    default String encodePassword(String password){
        return BcryptUtil.bcryptHash(password);
    }



    
}
