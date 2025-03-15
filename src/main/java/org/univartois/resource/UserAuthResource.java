package org.univartois.resource;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
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
import org.univartois.exception.TokenInvalidException;
import org.univartois.service.HomeService;
import org.univartois.service.UserService;
import org.univartois.utils.Constants;
import org.univartois.utils.ResponseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Path("/api/users")
public class UserAuthResource {
    @Context
    UriInfo uriInfo;

    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @Inject
    HomeService homeService;


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance passwordReset(String message);

        public static native TemplateInstance userVerified(String message);
    }


    @PermitAll
    @POST
    public RestResponse<ApiResponse<UserRegisterResponseDto>> registerUser(@Valid UserRegisterRequestDto userRegisterRequestDto) {
        UserRegisterResponseDto userRegisterResponseDto = userService.registerUser(userRegisterRequestDto);
        return RestResponse.status(RestResponse.Status.CREATED, ResponseUtil.success(userRegisterResponseDto, userRegisterResponseDto.getMessage(), RestResponse.Status.CREATED, uriInfo.getPath()));
    }

    @PermitAll
    @GET
    @Path("/verify")
    @Produces(MediaType.TEXT_HTML)
    public String verifyUser(@QueryParam("token") @NotBlank String token) {
        try {

            final VerificationAccountResponseDto verificationAccountResponseDto = userService.verifyAccount(token);
            return Templates.userVerified(verificationAccountResponseDto.getMessage()).render();
        } catch (TokenInvalidException exception) {
            return Templates.userVerified(exception.getMessage()).render();
        }
    }

    @PermitAll
    @POST
    @Path("/auth")
    public RestResponse<ApiResponse<UserAuthResponseDto>> login(@Valid UserAuthRequestDto userAuthRequestDto) {
        final UserAuthResponseDto userAuthResponseDto = userService.auth(userAuthRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(
                userAuthResponseDto, Constants.USER_AUTHENTICATED_MSG,
                RestResponse.Status.OK,
                uriInfo.getPath()
        ));
    }

    @PermitAll
    @PUT
    @Path("/userVerification")
    public RestResponse<ApiResponse<UserVerificationResponseDto>> userVerification(UserVerificationRequestDto userVerificationRequestDto) {
        final UserVerificationResponseDto userVerificationResponse = userService.userVerification(userVerificationRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(userVerificationResponse, userVerificationResponse.getMessage(), RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PermitAll
    @PUT
    @Path("/forgottenPassword")
    public RestResponse<ApiResponse<ForgotPasswordResponseDto>> forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        final ForgotPasswordResponseDto forgotPasswordResponse = userService.forgotPassword(forgotPasswordRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(forgotPasswordResponse, forgotPasswordResponse.getMessage(), RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PermitAll
    @GET
    @Path("/resetPassword")
    @Produces(MediaType.TEXT_HTML)
    public String resetPassword(@QueryParam("token") @NotBlank String token) {
        try {
            userService.resetPassword(token);
            return Templates.passwordReset(Constants.USER_PASSWORD_RESET_MSG).render();
        } catch (TokenInvalidException exception) {
            return Templates.passwordReset(exception.getMessage()).render();
        }
    }

    @PUT
    @Path("/me/profilePicture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Authenticated
    public RestResponse<ApiResponse<UpdateProfilePictureResponseDto>> updateProfilePicture(@RestForm("file") InputStream file) throws IOException {
        byte[] fileBytes = file.readAllBytes();
        file.close();
        final UpdateProfilePictureResponseDto profilePictureResponseDto = userService.updateProfilePicture(fileBytes);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(profilePictureResponseDto, Constants.USER_PROFILE_PICTURE_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @DELETE
    @Path("/me/profilePicture")
    @Authenticated
    public RestResponse<ApiResponse<Object>> deleteProfilePicture() {
        userService.deleteProfilePicture();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_PROFILE_PICTURE_DELETED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @Authenticated
    @GET
    @Path("/me")
    public RestResponse<ApiResponse<UserAuthResponseDto>> getProfile() {
        UserAuthResponseDto userAuthResponseDto = userService.getProfile();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(userAuthResponseDto, Constants.USER_PROFILE_RETRIEVED, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/me")
    @Authenticated
    public RestResponse<ApiResponse<UserAuthResponseDto>> updateProfile(@Valid UpdateAuthenticatedUserRequestDto dto) {
        UserAuthResponseDto userAuthResponseDto = userService.updateProfile(dto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(userAuthResponseDto, Constants.USER_PROFILE_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/me/password")
    @Authenticated
    public RestResponse<ApiResponse<Object>> updatePassword(@Valid UpdatePasswordRequestDto updatePasswordRequestDto) {
        userService.updatePassword(updatePasswordRequestDto);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_PASSWORD_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @DELETE
    @Authenticated
    @Path("/me")
    public RestResponse<ApiResponse<Object>> deleteProfile() {
        userService.deleteProfile();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_DELETED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Path("/me/homes")
    @Authenticated
    public RestResponse<ApiResponse<List<HomeResponseDto>>> getMyHomes() {
        List<HomeResponseDto> homeList = homeService.getMyHomes();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(homeList, Constants.USER_HOMES_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/me/constraints")
    @Authenticated
    public RestResponse<ApiResponse<DietaryConstraintsResponseDto>> updateDietaryConstraints(UpdateDietaryConstraintsRequestDto updateDietaryConstraintsRequestDto) {
        DietaryConstraintsResponseDto responseDto = userService.updateDietaryConstraints(updateDietaryConstraintsRequestDto);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(responseDto, Constants.USER_DIETARY_CONSTRAINTS_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Path("/me/constraints")
    @Authenticated
    public RestResponse<ApiResponse<DietaryConstraintsResponseDto>> getDietaryConstraints() {
        DietaryConstraintsResponseDto responseDto = userService.getDietaryConstraints();

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(responseDto, Constants.USER_DIETARY_CONSTRAINTS_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }
}
