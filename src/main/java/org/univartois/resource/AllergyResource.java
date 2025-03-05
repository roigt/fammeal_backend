package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.response.AllergyResponseDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.service.AllergyService;
import org.univartois.utils.Constants;
import org.univartois.utils.ResponseUtil;

import java.util.List;

@ApplicationScoped
@Path("/api/allergies")
public class AllergyResource {

    @Inject
    AllergyService allergyService;
    @Inject
    UriInfo uriInfo;


    @Authenticated
    @GET
    public RestResponse<ApiResponse<List<AllergyResponseDto>>> getAllergies(){
        List<AllergyResponseDto> allergies = allergyService.getAllergies();

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(allergies, Constants.ALLERGIES_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }


}
