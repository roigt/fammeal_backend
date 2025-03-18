package org.univartois.resource;

import io.quarkus.security.Authenticated;
import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.DateFormat;
import org.jboss.resteasy.reactive.RestResponse;
import org.univartois.dto.request.ProposedMealRequestDto;
import org.univartois.dto.response.ApiResponse;
import org.univartois.dto.response.MealProposalsByDateResponse;
import org.univartois.dto.response.ProposedMealResponseDto;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.service.ProposedMealService;
import org.univartois.utils.ResponseUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("/api/homes/{homeId}/proposedMeal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProposedMealResource {

    @Inject
    ProposedMealService proposedMealService;

    @Inject
    UriInfo uriInfo;


    /**
     * Récupérer tous les repas proposés dans une maison
     * @param homeId
     * @return
     */
    @GET
    @Authenticated
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
    public RestResponse<ApiResponse<List<ProposedMealResponseDto>>>  getProposedMeals(@PathParam("homeId") UUID homeId) {

        try{
            List<ProposedMealResponseDto> response = proposedMealService.getAllProposedMeals(homeId);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(response, "Liste des repas proposés récupérée.", RestResponse.Status.OK, uriInfo.getPath())
            );

        } catch (RuntimeException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId",
                            "Erreur lors de la récupération de la liste des repas proposés",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }
    }

    /**
     *
     * @param homeId
     * @param idMeal
     * @return
     */
    @GET
    @Path("/{idMeal}")
    @Authenticated
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
    public RestResponse<ApiResponse<List<ProposedMealResponseDto>>> getProposedMealByIdMeal(@PathParam("homeId") UUID homeId, @PathParam("idMeal") UUID idMeal) {

        try{
            List<ProposedMealResponseDto> responseByIdMeal = proposedMealService.getProposedMealsByMealId(homeId,idMeal);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(responseByIdMeal, "Le repas proposés est récupérée avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );

        } catch (RuntimeException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId,idMeal",
                            "Erreur lors de la récupération du repas proposés à partir de son Id",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }

    }

    /**
     * Recherche des plats proposés à partir de la date
     * @param homeId
     * @param date
     * @return
     */
    @GET
    @Path("/byDate")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
    public RestResponse<ApiResponse<MealProposalsByDateResponse>> getProposedMealsByDate(
            @PathParam("homeId") UUID homeId,
            @QueryParam("date") @NotNull @DateFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {

        if (date == null) {
            return RestResponse.status(Response.Status.BAD_REQUEST,
            ResponseUtil.error(
                    "Le champ date est obligatoire et doit etre sous la forme YY-MM-DD",
                    "date",
                    "Le champ date est obligatoire et doit etre sous la forme YY-MM-DD",
                    RestResponse.Status.BAD_REQUEST,
                    uriInfo.getPath()
                )

            );
        }

        try{
            MealProposalsByDateResponse proposedMeals = proposedMealService.searchMealByDate(homeId, date);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(proposedMeals, "Les repas proposés  a cette date sont récupérés avec succès.", RestResponse.Status.OK, uriInfo.getPath())
            );

        } catch (RuntimeException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "date",
                            "Erreur lors de la récupération des repas proposés à  cette date .",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }



    }
    /**
     * Ajouter une proposition de repas
     * @param proposedMealRequestDto
     * @return Response
     */
    @POST
    @Transactional
    @Authenticated
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
    public RestResponse<ApiResponse<ProposedMealResponseDto>>  proposeMeal(@PathParam("homeId") UUID homeId, ProposedMealRequestDto proposedMealRequestDto) {


        try {

            ProposedMealResponseDto responseDto = proposedMealService.proposeMeal(homeId,proposedMealRequestDto);

            return RestResponse.status(
                    RestResponse.Status.CREATED,
                    ResponseUtil.success(responseDto, "Repas ajouté a la liste des repas proposé.", RestResponse.Status.CREATED, uriInfo.getPath())
            );
        } catch (RuntimeException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "mealId",
                            "Erreur lors de l'ajout du repas à la liste des repas proposés",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }
    }



    /**
     * Mettre à jour une proposition de repas
     * @param recipeId
     * @param mealId
     * @param proposerId
     * @param proposedMealRequestDto
     * @return Response
     */
//    @PUT
//    @Authenticated
//    @Path("/{recipeId}/{mealId}/{proposerId}")
//    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdExpression = "homeId")
//    public RestResponse<ApiResponse<ProposedMealResponseDto>> updateProposedMeal(
//            @PathParam("homeId") UUID homeId,
//            @PathParam("recipeId") UUID recipeId,
//            @PathParam("mealId") UUID mealId,
//            @PathParam("proposerId") UUID proposerId,
//            ProposedMealRequestDto proposedMealRequestDto) {
//
//        try {
//            ProposedMealResponseDto responseDto = proposedMealService.updateProposedMeal(recipeId, mealId, proposerId, proposedMealRequestDto);
//
//            return RestResponse.status(
//                    RestResponse.Status.OK,
//                    ResponseUtil.success(responseDto, "Repas proposé mise a jour avec succès", RestResponse.Status.OK, uriInfo.getPath())
//            );
//        } catch (ResourceNotFoundException e) {
//            return RestResponse.status(
//                    RestResponse.Status.NOT_FOUND,
//                    ResponseUtil.error(
//                            e.getMessage(),
//                            "homeId, recipeId, mealId, proposerId",
//                            "Erreur lors de la mise a jour du repas repas proposé",
//                            RestResponse.Status.NOT_FOUND,
//                            uriInfo.getPath()
//                    )
//            );
//        }
//
//
//    }

    /**
     * Supprimer une proposition de repas

     * @return Response
     */
    @DELETE
    @Authenticated
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public RestResponse<ApiResponse<Object>> deleteProposedMeal(@PathParam("homeId") UUID homeId, ProposedMealRequestDto proposedMealRequestDto){
        proposedMealService.deleteProposedMeal(homeId,proposedMealRequestDto);
        return RestResponse.status(RestResponse.Status.NO_CONTENT, ResponseUtil.success("suppression réussie", "Repas Proposée Supprimée avec succès.", RestResponse.Status.NO_CONTENT, uriInfo.getPath()));

    }
}
