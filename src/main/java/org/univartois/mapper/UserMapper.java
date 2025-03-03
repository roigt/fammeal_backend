package org.univartois.mapper;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.univartois.dto.request.UpdateAuthenticatedUserRequestDto;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.HomeMemberResponseDto;
import org.univartois.dto.response.UserAuthResponseDto;
import org.univartois.entity.UserEntity;

import java.util.Map;

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
    @Mapping(source = "user.profilePictureUrl", target = "profilePictureUrl")
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "roles", target = "roles")
    UserAuthResponseDto toAuthResponseDto(UserEntity user, String accessToken, Map<String, String> roles);

    @Mapping(source = "role", target = "role")
    HomeMemberResponseDto toHomeMemberResponseDto(UserEntity user, String role);

    void updateEntity(@MappingTarget UserEntity user, UpdateAuthenticatedUserRequestDto dto);

    @Named("encodePassword")
    default String encodePassword(String password) {
        return BcryptUtil.bcryptHash(password);
    }


}
