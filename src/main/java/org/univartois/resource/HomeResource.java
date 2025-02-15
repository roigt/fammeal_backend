package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.annotation.HomePermissionsAllowed;
import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.enums.HomeRoleType;
import org.univartois.service.HomeService;
import org.univartois.utils.ResponseUtil;

import java.util.List;
import java.util.UUID;

@Slf4j
@Path("/api/homes")
public class HomeResource {

    @Inject
    HomeService homeService;

    @Inject
    JsonWebToken jwt;


    @Inject
    UriInfo uriInfo;


    @GET
    @Authenticated
    public RestResponse<ApiResponse<List<HomeResponseDto>>> getHomes() {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        List<HomeResponseDto> homeList = homeService.getUserHomes(userUuid); //
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(homeList, "La liste de vos maisons a été récupérée", RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.MEMBER_ROLE}, homeIdParamName = "homeId")
    @Path("/{homeId}")
    public RestResponse<ApiResponse<HomeResponseDto>> getHome(@PathParam("homeId") UUID homeId) {
        final HomeResponseDto home = homeService.getHomeById(homeId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(home, "Détails de la maison récupérés.", RestResponse.Status.OK, uriInfo.getPath()));
    }

    @POST
    @Authenticated
    public RestResponse<ApiResponse<HomeResponseDto>> createHome(CreateHomeRequestDto createHomeRequestDto) {
        final HomeResponseDto home = homeService.createHome(createHomeRequestDto);

        return RestResponse.status(RestResponse.Status.CREATED, ResponseUtil.success(home, "La maison a été créée avec succès", RestResponse.Status.CREATED, uriInfo.getPath()));
    }

}
