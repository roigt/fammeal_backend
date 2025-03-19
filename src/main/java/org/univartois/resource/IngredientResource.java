package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.IngredientRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.IngredientResponseDto;
import org.univartois.exception.IngredientAlreadyExistsException;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.repository.IngredientRepository;
import org.univartois.service.IngredientService;
import org.univartois.utils.ResponseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Path("/api/ingredients")
public class IngredientResource {

    @Inject
    IngredientService ingredientService;

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    UriInfo uriInfo;

    @GET
    @Authenticated
    public RestResponse<ApiResponse<List<IngredientResponseDto>>> getIngredients() {
        List<IngredientResponseDto> ingredients = ingredientService.getAllIngredients();
        return RestResponse.status(RestResponse.Status.OK, ResponseUtil.success(ingredients, "La liste des ingrédients a été récupérée", RestResponse.Status.OK, uriInfo.getPath()));
    }

    @GET
    @Authenticated
    @Path("/{ingredientId}")
    public RestResponse<ApiResponse<IngredientResponseDto>> getIngredientById(@PathParam("ingredientId") UUID ingredientId) {

        try {

            final IngredientResponseDto ingredient = ingredientService.getIngredientById(ingredientId);


            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(ingredient, "L'ingrédient a été récupéré avec succès", RestResponse.Status.OK, uriInfo.getPath())
            );
        } catch (ResourceNotFoundException e) {

            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "ingredientName",
                            "Erreur lors de la récupération de l'ingrédient",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }
    }


//
//    @POST
//    @Authenticated
//    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
//    public RestResponse<ApiResponse<IngredientResponseDto>> createIngredient(IngredientRequestDto IngredientRequestDto) {
//        try {
//
//            final IngredientResponseDto ingredient = ingredientService.createIngredient(IngredientRequestDto);
//             System.out.println("unite recu est "+ingredient.getIdUnit().getUnitName());
//
//            return RestResponse.status(
//                    RestResponse.Status.CREATED,
//                    ResponseUtil.success(ingredient, "L'ingrédient a été créé avec succès", RestResponse.Status.CREATED, uriInfo.getPath())
//            );
//        } catch (IngredientAlreadyExistsException e) {
//
//            return RestResponse.status(
//                    RestResponse.Status.CONFLICT,
//                    ResponseUtil.error(
//                            e.getMessage(),
//                            "ingredientName",
//                            "Erreur lors de la création de l'ingrédient",
//                            RestResponse.Status.CONFLICT,
//                            uriInfo.getPath()
//                    )
//            );
//        }
//    }
//
//    @PUT
//    @Authenticated
//    @Path("/{ingredientId}")
//    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
//    public RestResponse<ApiResponse<IngredientResponseDto>> updateIngredient(@PathParam("ingredientId") UUID ingredientId,
//                                                                             IngredientRequestDto ingredientRequestDto) {
//        try {
//
//            final IngredientResponseDto updatedIngredient = ingredientService.updateIngredient(ingredientId, ingredientRequestDto);
//
//            return RestResponse.status(
//                    RestResponse.Status.OK,
//                    ResponseUtil.success(updatedIngredient, "L'ingrédient a été mis à jour avec succès", RestResponse.Status.OK, uriInfo.getPath())
//            );
//        } catch (ResourceNotFoundException e) {
//            return RestResponse.status(
//                    RestResponse.Status.NOT_FOUND,
//                    ResponseUtil.error(
//                            e.getMessage(),
//                            "ingredientId",
//                            "Erreur lors de la mise à jour de l'ingrédient",
//                            RestResponse.Status.NOT_FOUND,
//                            uriInfo.getPath()
//                    )
//            );
//        }
//    }
//
//    @DELETE
//    @Authenticated
//    @Path("/{ingredientId}")
//    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
//    public RestResponse<ApiResponse<Void>> deleteIngredient(@PathParam("ingredientId") UUID ingredientId) {
//        try {
//
//            ingredientService.deleteIngredient(ingredientId);
//
//            return RestResponse.status(
//                    RestResponse.Status.NO_CONTENT,
//                    ResponseUtil.success(null, "L'ingrédient a été supprimé avec succès", RestResponse.Status.NO_CONTENT, uriInfo.getPath())
//            );
//        } catch (ResourceNotFoundException e) {
//            return RestResponse.status(
//                    RestResponse.Status.NOT_FOUND,
//                    ResponseUtil.error(
//                            e.getMessage(),
//                            "ingredientId",
//                            "Erreur lors de la suppression de l'ingrédient",
//                            RestResponse.Status.NOT_FOUND,
//                            uriInfo.getPath()
//                    )
//            );
//        }
//    }
}
