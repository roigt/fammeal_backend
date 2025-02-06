package org.univartois.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.ApiResponse;
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


    @POST
    public RestResponse<ApiResponse<UserRegisterResponseDto>> registerUser(@Valid UserRegisterRequestDto userRegisterRequestDto) {
        UserRegisterResponseDto userRegisterResponseDto = userAuthService.registerUser(userRegisterRequestDto);
        return RestResponse.status(RestResponse.Status.CREATED, ResponseUtil.success(userRegisterResponseDto, userRegisterResponseDto.getMessage(), RestResponse.Status.CREATED, uriInfo.getPath()));
    }

    @GET
    @Path("/verify")
    public RestResponse<ApiResponse<VerificationAccountResponseDto>> verifyUser(@QueryParam("token") @NotBlank String token) {
        final VerificationAccountResponseDto verificationAccountResponseDto = userAuthService.verifyAccount(token);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(verificationAccountResponseDto, verificationAccountResponseDto.getMessage(), RestResponse.Status.OK, uriInfo.getPath()));
    }


    @Path("/me")
    @POST
    public void throwException() {
        throw new UnsupportedOperationException("a problem occured");
    }
}
