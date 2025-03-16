package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

import org.jboss.resteasy.reactive.RestResponse;


import org.univartois.dto.request.PantryIngredientRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.PantryIngredientResponseDto;
import org.univartois.enums.HomeRoleType;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.repository.IngredientRepository;
import org.univartois.service.PantryIngredientService;
import org.univartois.utils.ResponseUtil;

import java.util.List;
import java.util.UUID;

@Slf4j
@Path("/api/homes/{homeId}/pantryIngredients")

public class PantryIngredientResource {

    @Inject
    PantryIngredientService pantryIngredientService;

    @Inject
    JsonWebToken jwt;

    @Inject
    UriInfo uriInfo;
    @Inject
    IngredientRepository ingredientRepository;


    /**
     * Récupérer la liste des ingrédients du garde-manger d'une maison
     */
    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
   // @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<List<PantryIngredientResponseDto>>> getPantryIngredients(@PathParam("homeId") UUID homeId) {
        try {
            List<PantryIngredientResponseDto> pantryIngredients = pantryIngredientService.getPantryIngredientsByHomeId(homeId);

            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(pantryIngredients, "Liste des ingrédients récupérée.", RestResponse.Status.OK, uriInfo.getPath())
            );
        } catch (ResourceNotFoundException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId",
                            "Erreur lors de la récupération des ingrédients du garde-manger",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }
    }
    /**
     * Récupérer les infos sur un ingrédients du garde-manger
     * */
    @GET
    @Authenticated
    @Path("/{ingredientInPantryId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // @HomePermissionsAllowed(value = {HomeRoleType.Constants.ADMIN_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<PantryIngredientResponseDto>> getPantryIngredientsById(@PathParam("homeId") UUID homeId, @PathParam("ingredientInPantryId") UUID ingredientPantryId) {
        try {
            PantryIngredientResponseDto pantryIngredients = pantryIngredientService.getPantryIngredientByPantryIngredientIdAndHomeId(homeId,ingredientPantryId);

            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(pantryIngredients, "Liste des ingrédients récupérée.", RestResponse.Status.OK, uriInfo.getPath())
            );
        } catch (ResourceNotFoundException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId",
                            "Erreur lors de la récupération des ingrédients du garde-manger",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }
    }




    /**
     * Ajouter un ingrédient au garde-manger
     */
    @POST
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<PantryIngredientResponseDto>> addPantryIngredient(@PathParam("homeId") UUID homeId,PantryIngredientRequestDto pantryIngredientRequestDto) {
        try {

            PantryIngredientResponseDto responseDto = pantryIngredientService.addPantryIngredient(homeId,pantryIngredientRequestDto);
            return RestResponse.status(
                    RestResponse.Status.CREATED,
                    ResponseUtil.success(responseDto, "Ingrédient ajouté au garde-manger.", RestResponse.Status.CREATED, uriInfo.getPath())
            );
        } catch (ResourceNotFoundException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId,ingredientId",
                            "Erreur lors de l'ajout de l'ingrédient au garde-manger",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }
    }

    /**
     * Mettre à jour un ingrédient du garde-manger
     */
    @PUT
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @HomePermissionsAllowed(value = {HomeRoleType.Constants.MEMBER_ROLE}, homeIdParamName = "homeId")
    @Path("/{ingredientInPantryId}")
    public RestResponse<ApiResponse<PantryIngredientResponseDto>> updatePantryIngredient(@PathParam("homeId") UUID homeId, @PathParam("ingredientInPantryId") UUID ingredientInPantryId, PantryIngredientRequestDto pantryIngredientRequestDto) {
        try {

            PantryIngredientResponseDto responseDto = pantryIngredientService.updatePantryIngredient(ingredientInPantryId, pantryIngredientRequestDto);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseDto, "Ingrédient mis à jour avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );
        } catch (ResourceNotFoundException e) {
            return RestResponse.status(
                    RestResponse.Status.CONFLICT,
                    ResponseUtil.error(
                            e.getMessage(),
                            "ingredientInPantryId",
                            "Erreur lors de la mise à jour de l'ingrédient du garde-manger",
                            RestResponse.Status.CONFLICT,
                            uriInfo.getPath()
                    )
            );
        }
    }


    /**
     * Supprimer un ingrédient du garde-manger
     */
    @DELETE
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
// @HomePermissionsAllowed(value = {HomeRoleType.Constants.MEMBER_ROLE}, homeIdParamName = "homeId")
    @Path("/{ingredientInPantryId}")
    public RestResponse<ApiResponse<Object>> deletePantryIngredient(@PathParam("homeId") UUID homeId,@PathParam("ingredientInPantryId") UUID ingredientInPantryId) {
        pantryIngredientService.deletePantryIngredient(ingredientInPantryId);
        return RestResponse.status(RestResponse.Status.NO_CONTENT, ResponseUtil.success(null, "Ingrédient supprimé avec succès.", RestResponse.Status.NO_CONTENT, uriInfo.getPath()));

    }
}
