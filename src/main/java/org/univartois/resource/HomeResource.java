package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.annotation.security.HomePermissionsAllowed;
import org.univartois.dto.request.*;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.DietaryConstraintsResponseDto;
import org.univartois.dto.response.HomeMemberResponseDto;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.enums.HomeRoleType;
import org.univartois.service.HomeService;
import org.univartois.utils.Constants;
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
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.MEMBER_ROLE}, homeIdParamName = "homeId")
    @Path("/{homeId}")
    public RestResponse<ApiResponse<HomeResponseDto>> getHome(@PathParam("homeId") UUID homeId) {
        final HomeResponseDto home = homeService.getHomeById(homeId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(home, Constants.HOME_DETAILS_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @POST
    @Authenticated
    public RestResponse<ApiResponse<HomeResponseDto>> createHome(@Valid CreateHomeRequestDto createHomeRequestDto) {
        final HomeResponseDto home = homeService.createHome(createHomeRequestDto);

        return RestResponse.status(RestResponse.Status.CREATED, ResponseUtil.success(home, Constants.HOME_CREATED_MSG, RestResponse.Status.CREATED, uriInfo.getPath()));
    }

    @PUT
    @Path("{homeId}")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<HomeResponseDto>> updateHome(@PathParam("homeId") UUID homeId, @Valid UpdateHomeRequestDto updateHomeRequestDto){
        final HomeResponseDto home = homeService.updateHome(homeId,updateHomeRequestDto);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(home, Constants.HOME_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath() ));
    }


    @DELETE
    @Path("/{homeId}/leave")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.MEMBER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<Object>> leaveHome(@PathParam("homeId") UUID homeId) {
        homeService.leaveHome(homeId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.HOME_LEFT_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @POST
    @Path("/{homeId}/members")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<Object>> addHomeMember(@PathParam("homeId") UUID homeId, @Valid AddHomeMemberRequestDto addHomeMemberRequestDto) {
        homeService.addHomeMember(homeId, addHomeMemberRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.USER_ADDED_TO_HOME_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Path("/{homeId}/members")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<List<HomeMemberResponseDto>>> getHomeMembers(@PathParam("homeId") UUID homeId) {
        final List<HomeMemberResponseDto> homeMembers = homeService.getHomeMembers(homeId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(homeMembers, Constants.HOME_MEMBERS_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Path("/{homeId}/members/{userId}")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<HomeMemberResponseDto>> getHomeMembers(@PathParam("homeId") UUID homeId, @PathParam("userId") UUID userId) {
        final HomeMemberResponseDto homeMember = homeService.getHomeMember(homeId, userId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(homeMember, Constants.HOME_MEMBER_DETAILS_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @PUT
    @Path("/{homeId}/members/{userId}")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<HomeMemberResponseDto>> updateHomeMember(@PathParam("homeId") UUID homeId, @PathParam("userId") UUID userId, UpdateHomeMemberRequestDto updateHomeMemberRequestDto) {
        HomeMemberResponseDto updatedHomeMember = homeService.updateHomeMember(homeId, userId, updateHomeMemberRequestDto);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(updatedHomeMember, Constants.HOME_MEMBER_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @DELETE
    @Path("/{homeId}/members/{userId}")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<Object>> deleteHomeMember(@PathParam("homeId") UUID homeId, @PathParam("userId") UUID userId) {
        homeService.deleteHomeMember(homeId, userId);
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.HOME_MEMBER_DELETED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }


    @PUT
    @Path("/{homeId}/constraints")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<DietaryConstraintsResponseDto>> updateDietaryConstraints(@PathParam("homeId") UUID homeId,UpdateDietaryConstraintsRequestDto updateDietaryConstraintsRequestDto) {
        DietaryConstraintsResponseDto responseDto = homeService.updateDietaryConstraints(homeId,updateDietaryConstraintsRequestDto);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(responseDto, Constants.HOME_DIETARY_CONSTRAINTS_UPDATED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Path("/{homeId}/constraints")
    @Authenticated
    public RestResponse<ApiResponse<DietaryConstraintsResponseDto>> getDietaryConstraints(@PathParam("homeId") UUID homeId) {
        DietaryConstraintsResponseDto responseDto = homeService.getDietaryConstraints(homeId);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(responseDto, Constants.HOME_DIETARY_CONSTRAINTS_RETRIEVED_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }


    @PUT
    @Path("/{homeId}/mealGeneration")
    @Authenticated
    @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<Object>> toggleMealGeneration(@PathParam("homeId") UUID homeId, @QueryParam("lunch") boolean lunch){
        homeService.toggleMealGeneration(homeId, lunch);

        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(null, Constants.HOME_TOGGLE_GENERATION_MSG, RestResponse.Status.OK, uriInfo.getPath()));
    }
}
