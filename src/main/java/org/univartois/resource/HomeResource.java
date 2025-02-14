package org.univartois.resource;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.service.HomeService;
import org.univartois.utils.ResponseUtil;

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

    private String getResponseString(SecurityContext ctx, SecurityIdentity securityIdentity) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }

        log.info("security identity: {}", securityIdentity.getAttributes().toString());

        return String.format("hello %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s,"+
                "permission: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt(), securityIdentity.getAttributes().get("permissions").toString());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }

    @GET
    @Authenticated
    public String getHomes(@Context SecurityContext securityContext,  @Context SecurityIdentity securityIdentity) {
        return getResponseString(securityContext, securityIdentity);
    }

    @GET
    @Authenticated
    @Path("/{homeId}")
    public RestResponse<ApiResponse<HomeResponseDto>> getHome(@PathParam("homeId") UUID homeId){
        final HomeResponseDto home = homeService.getHomeById(homeId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(home, "Détails de la maison récupérés.", RestResponse.Status.OK,uriInfo.getPath()));
    }

    @POST
    @Authenticated
    public RestResponse<ApiResponse<HomeResponseDto>> createHome(CreateHomeRequestDto createHomeRequestDto){
        final HomeResponseDto home = homeService.createHome(createHomeRequestDto);

        return RestResponse.status(RestResponse.Status.CREATED, ResponseUtil.success(home, "La maison a été créée avec succès", RestResponse.Status.CREATED,uriInfo.getPath()));
    }

}
