package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.HomeRoleTypeResponseDto;
import org.univartois.enums.HomeRoleType;
import org.univartois.service.RoleService;
import org.univartois.utils.Constants;
import org.univartois.utils.ResponseUtil;

import java.util.List;

@Path("/api/roles")
@ApplicationScoped
public class HomeRoleResource {

    @Inject
    RoleService roleService;

    @Inject
    UriInfo uriInfo;

    @Authenticated
    @GET
    public RestResponse<ApiResponse<List<HomeRoleTypeResponseDto>>> getHomeRoles(){
        List<HomeRoleTypeResponseDto> homeRoles = roleService.getHomeRoles();

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(homeRoles, Constants.HOME_ROLES_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }
}
