package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.*;
import org.univartois.dto.response.*;
import org.univartois.service.HomeService;
import org.univartois.service.UserAuthService;
import org.univartois.utils.Constants;
import org.univartois.utils.ResponseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Path("/api/users")
public class UserAuthResource {
    @Context
    UriInfo uriInfo;

    @Inject
    UserAuthService userAuthService;

    @Inject
    JsonWebToken jwt;

    @Inject
    HomeService homeService;


    @PermitAll
    @POST
    public RestResponse<ApiResponse<UserRegisterResponseDto>> registerUser(@Valid UserRegisterRequestDto userRegisterRequestDto) {
        UserRegisterResponseDto userRegisterResponseDto = userAuthService.registerUser(userRegisterRequestDto);
        return RestResponse.status(RestResponse.Status.CREATED, ResponseUtil.success(userRegisterResponseDto, userRegisterResponseDto.getMessage(), RestResponse.Status.CREATED, uriInfo.getPath()));
    }

    @PermitAll
    @GET
    @Path("/verify")
    public RestResponse<ApiResponse<VerificationAccountResponseDto>> verifyUser(@QueryParam("token") @NotBlank String token) {
        final VerificationAccountResponseDto verificationAccountResponseDto = userAuthService.verifyAccount(token);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(verificationAccountResponseDto, verificationAccountResponseDto.getMessage(), RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PermitAll
    @POST
    @Path("/auth")
    public RestResponse<ApiResponse<UserAuthResponseDto>> login(@Valid UserAuthRequestDto userAuthRequestDto) {
        final UserAuthResponseDto userAuthResponseDto = userAuthService.auth(userAuthRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(
                userAuthResponseDto, "authentification réussie. Bienvenue dans votre espace fammeal",
                RestResponse.Status.OK,
                uriInfo.getPath()
        ));
    }

    @PermitAll
    @PUT
    @Path("/userVerification")
    public RestResponse<ApiResponse<UserVerificationResponseDto>> userVerification(UserVerificationRequestDto userVerificationRequestDto) {
        final UserVerificationResponseDto userVerificationResponse = userAuthService.userVerification(userVerificationRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(userVerificationResponse, userVerificationResponse.getMessage(), RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PermitAll
    @PUT
    @Path("/forgottenPassword")
    public RestResponse<ApiResponse<ForgotPasswordResponseDto>> forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        final ForgotPasswordResponseDto forgotPasswordResponse = userAuthService.forgotPassword(forgotPasswordRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(forgotPasswordResponse, forgotPasswordResponse.getMessage(), RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PermitAll
    @GET
    @Path("/resetPassword")
    public RestResponse<ApiResponse<Object>> resetPassword(@QueryParam("token") @NotBlank String token) {
        userAuthService.resetPassword(token);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, "votre nouveau mot de passe est réinitialisé. Veuillez vérifier votre adresse mail", RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/me/profilePicture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Authenticated
    public RestResponse<ApiResponse<UpdateProfilePictureResponseDto>> updateProfilePicture(@RestForm("file") InputStream file) throws IOException {
        byte[] fileBytes = file.readAllBytes();
        file.close();
        final UpdateProfilePictureResponseDto profilePictureResponseDto = userAuthService.updateProfilePicture(fileBytes);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(profilePictureResponseDto, Constants.USER_PROFILE_PICTURE_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @DELETE
    @Path("/me/profilePicture")
    @Authenticated
    public RestResponse<ApiResponse<Object>> deleteProfilePicture() {
        userAuthService.deleteProfilePicture();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_PROFILE_PICTURE_DELETED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @Authenticated
    @GET
    @Path("/me")
    public RestResponse<ApiResponse<UserAuthResponseDto>> getCurrentAuthenticatedUser() {
        UserAuthResponseDto userAuthResponseDto = userAuthService.getCurentAuthenticatedUser();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(userAuthResponseDto, "informations de votre compte", RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/me")
    @Authenticated
    public RestResponse<ApiResponse<UserAuthResponseDto>> updateCurrentAuthenticatedUser(@Valid UpdateAuthenticatedUserRequestDto dto){
        UserAuthResponseDto userAuthResponseDto = userAuthService.updateCurrentAuthenticatedUser(dto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(userAuthResponseDto, Constants.USER_PROFILE_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/me/password")
    @Authenticated
    public RestResponse<ApiResponse<Object>> updatePassword(@Valid UpdatePasswordRequestDto updatePasswordRequestDto){
        userAuthService.updatePassword(updatePasswordRequestDto);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_PASSWORD_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @DELETE
    @Authenticated
    @Path("/me")
    public RestResponse<ApiResponse<Object>> deleteCurrentAuthenticatedUser(){
        userAuthService.deleteCurrentAuthenticatedUser();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_DELETED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Path("/me/homes")
    @Authenticated
    public RestResponse<ApiResponse<List<HomeResponseDto>>> getHomes() {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        List<HomeResponseDto> homeList = homeService.getUserHomes(userUuid);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(homeList, "La liste de vos maisons a été récupérée", RestResponse.Status.OK, uriInfo.getPath()));
    }


}
