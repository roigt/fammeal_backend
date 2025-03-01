package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.RecipeRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.RecipeResponseDto;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.RecipeMapper;
import org.univartois.repository.RecipeRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.univartois.service.RecipeService;
import org.univartois.utils.ResponseUtil;

import java.util.List;
import java.util.UUID;

@Path("/api/recipes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Recipes")
public class RecipeResource {

    @Inject
    RecipeRepository recipeRepository;

    @Inject
    RecipeMapper recipeMapper;

    @Inject
    RecipeService recipeService;

    @Inject
    JsonWebToken jwt;

    @Inject
    UriInfo uriInfo;

    /**
     * Récupérer la liste de toutes les recettes
     * @return
     */
    @GET
    @Authenticated
    @Operation(summary = "Get all recipes", description = "Returns a list of all recipes.")
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<List<RecipeResponseDto>>> getAllRecipes() {
        try{
            List<RecipeResponseDto> responseDto = recipeService.getAllRecipes();

            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseDto, "Liste des Recettes récupérée avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );

        }catch (ResourceNotFoundException e){
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idRecette",
                            "Erreur lors de la récupération de la recette ",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }

    }

    /**
     * Récupérer la liste de toutes les recettes publiques
     * @return
     */
    @GET
    @Path("/publics")
    @Authenticated
    @Operation(summary = "Get all public recipes", description = "Returns a list of all public recipes.")
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<List<RecipeResponseDto>>> getPublicRecipes() {
        try{
            List<RecipeResponseDto> responseDto = recipeService.getAllPublicRecipes();

            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseDto, "Liste des Recettes publiques récupérée avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );

        }catch (ResourceNotFoundException e){
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idRecette",
                            "Erreur lors de la récupération des recettes publiques",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }

    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public Response searchRecipes(
            @QueryParam("keywords") List<String> keywords,
            @QueryParam("ingredients") List<String> ingredientIds,
            @QueryParam("vegetarian") Boolean vegetarian,
            @QueryParam("covers") Integer covers,
            @QueryParam("lunch_box") Boolean lunchBox
            ) {

        List<RecipeResponseDto> recipes = recipeService.searchRecipes(keywords, ingredientIds, vegetarian, covers, lunchBox);
        return Response.ok(recipes).build();
    }


    /**
     *  Créer une nouvelle recette
     * @param recipeRequestDto
     * @return
     */
    @Authenticated
    @POST
    @Operation(summary = "Create a new recipe", description = "Creates a new recipe.")
    @Transactional
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<RecipeResponseDto>>  createRecipe(RecipeRequestDto recipeRequestDto) {
        try{
            RecipeResponseDto responseDto = recipeService.createRecipe(recipeRequestDto);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseDto, "Recette créer avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );
        }catch (ResourceNotFoundException e){
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idRecette",
                            "Erreur lors de la création de la recette",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );

        }

    }

    /**
     * Récupérer les détails d'une recette par son ID
     * @param idRecipe
     * @return
     */
    @GET
    @Authenticated
    @Path("/{idRecipe}")
    @Operation(summary = "Get recipe by ID", description = "Returns the details of a recipe.")
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<RecipeResponseDto>>  getRecipeById(@PathParam("idRecipe") UUID idRecipe) {
        try{
            RecipeResponseDto responseDto = recipeService.getRecipeById(idRecipe);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseDto, "Recette récupérer avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );
        }catch (ResourceNotFoundException e){
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idRecipe",
                            "Erreur lors de la récupération de la recette",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }


    }

    /**
     * Mettre à jour une recette existante
     * @param idRecipe
     * @param recipeRequestDto
     * @return
     */
    @PUT
    @Authenticated
    @Path("/{idRecipe}")
    @Operation(summary = "Update recipe", description = "Updates an existing recipe.")
    @Transactional
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<RecipeResponseDto>> updateRecipe(@PathParam("idRecipe") UUID idRecipe, RecipeRequestDto recipeRequestDto) {
        try{
            RecipeResponseDto responseDto =recipeService.updateRecipe(idRecipe, recipeRequestDto);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseDto, "Recette Modifié avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );
        }catch (ResourceNotFoundException e){
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idRecipe",
                            "Erreur lors de la Modification de la recette",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }


    }

    /**
     * Supprimer une recette (safe delete)
     * @param idRecipe
     * @return
     */
    @DELETE
    @Authenticated
    @Path("/{idRecipe}")
    @Operation(summary = "Delete recipe", description = "Deletes a recipe (soft delete).")
    @Transactional
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public Object deleteRecipe(@PathParam("idRecipe") UUID idRecipe) {
        try{
            recipeService.deleteRecipe(idRecipe);

            return RestResponse.status(
                    RestResponse.Status.NO_CONTENT,
                    ResponseUtil.success("suppression réussie", "Recette Supprimée avec succès.", RestResponse.Status.NO_CONTENT, uriInfo.getPath())
            );
        }catch (ResourceNotFoundException e){
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idRecipe",
                            "Erreur lors de la Modification de la recette",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }

    }
}

