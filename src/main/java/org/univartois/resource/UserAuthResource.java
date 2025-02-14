package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.UserAuthRequestDto;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.UserAuthResponseDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.dto.response.VerificationAccountResponseDto;
import org.univartois.service.UserAuthService;
import org.univartois.utils.ResponseUtil;

@Path("/api/users")
public class UserAuthResource {
    @Context
    UriInfo uriInfo;

    @Inject
    UserAuthService userAuthService;


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

    @Authenticated
    @Path("/me")
    @POST
    public void throwException() {
        throw new UnsupportedOperationException("a problem occured");
    }
}
