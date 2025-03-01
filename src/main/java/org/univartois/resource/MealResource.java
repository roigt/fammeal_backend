package org.univartois.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import org.univartois.dto.request.MealRequestDto;
import org.univartois.dto.response.*;
import org.univartois.enums.HomeRoleType;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.service.MealService;
import org.univartois.utils.ResponseUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/api/homes/{idHome}/meals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MealResource {

    @Inject
    MealService mealService;
    @Inject
    UriInfo uriInfo;

    /**
     * Récupérer le planning des repas pour une maison
     * @param homeId l'ID de la maison
     * @return Response contenant la liste des repas
     */
    @GET
    @Authenticated
  //  @HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public  RestResponse<ApiResponse<List<MealResponseDto>>>  getMeals(@PathParam("idHome") UUID homeId) {
        try {
            List<MealResponseDto> meals = mealService.getMealsByHome(homeId);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(meals, "Liste des repas récupérée.", RestResponse.Status.OK, uriInfo.getPath())
            );
        } catch (RuntimeException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId",
                            "Erreur lors de la récupération de la liste des repas planifié dans une maison",
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
    //  @HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public  RestResponse<ApiResponse<Optional<MealResponseDto>>>  getMeals(@PathParam("idHome") UUID homeId, @PathParam("idMeal") UUID idMeal) {
        try {
            Optional<MealResponseDto> meals = mealService.getMealsByHomeAndIdMeal(homeId,idMeal);
            return RestResponse.status(
                    RestResponse.Status.OK,
                    ResponseUtil.success(meals, "Liste des repas récupérée.", RestResponse.Status.OK, uriInfo.getPath())
            );
        } catch (RuntimeException e) {
            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "homeId",
                            "Erreur lors de la récupération de la liste des repas planifié dans une maison",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }

    }

    @GET
    @Path("/DateTo")
    @Transactional
    @Authenticated
    public MealResponseFromDateToDto getMealFromDateToDate(@PathParam("idHome") UUID idHome,
                                                           @QueryParam("from") LocalDate from,
                                                           @QueryParam("to") LocalDate to){
        return mealService.getMealDateTo(idHome,from,to) ;

    }


    /**
     * Planifier un nouveau repas pour une maison
     * @param idHome l'ID de la maison
     * @param mealRequestDto le DTO du repas à créer
     * @return Response contenant le repas planifié
     */
    @POST
    @Authenticated
    @Transactional
   // @HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public  RestResponse<ApiResponse<MealResponseDto>>  createMeal(@PathParam("idHome") UUID idHome, MealRequestDto mealRequestDto) {
        try {

            MealResponseDto createdMeal = mealService.planMeal(idHome, mealRequestDto);


            return RestResponse.status(
                    RestResponse.Status.CREATED,
                    ResponseUtil.success(createdMeal, "Le repas a été créé avec succès", RestResponse.Status.CREATED, uriInfo.getPath())
            );
        } catch (RuntimeException e) {

            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idHome",
                            "Erreur lors de la création du repas dans une maison ",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }


    }

    /**
     * Mettre à jour un repas pour une maison
     * @param idHome l'ID de la maison
     * @param idMeal l'ID du repas à mettre à jour
     * @param mealRequestDto le DTO du repas mis à jour
     * @return Response contenant le repas mis à jour
     */
    @PUT
    @Authenticated
    @Path("/{idMeal}")
    @Transactional
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public  RestResponse<ApiResponse<MealResponseDto>>  updateMeal(@PathParam("idHome") UUID idHome, @PathParam("idMeal") UUID idMeal, MealRequestDto mealRequestDto) {
        try {

            MealResponseDto updatedMeal = mealService.updateMeal(idHome, idMeal, mealRequestDto);


            return RestResponse.status(
                    RestResponse.Status.CREATED,
                    ResponseUtil.success(updatedMeal, "Le repas a été modifié avec succès", RestResponse.Status.CREATED, uriInfo.getPath())
            );
        } catch (RuntimeException e) {

            return RestResponse.status(
                    RestResponse.Status.NOT_FOUND,
                    ResponseUtil.error(
                            e.getMessage(),
                            "idHome",
                            "Erreur lors de la modification du repas dans une maison ",
                            RestResponse.Status.NOT_FOUND,
                            uriInfo.getPath()
                    )
            );
        }


    }

    /**
     * Supprimer un repas pour une maison
     * @param idHome l'ID de la maison
     * @param idMeal l'ID du repas à supprimer
     * @return Response indiquant le succès ou l'échec de la suppression
     */
    @DELETE
    @Authenticated
    @Path("/{idMeal}")
    @Transactional
    //@HomePermissionsAllowed(value = {HomeRoleType.Constants.GARDE_MANGER_ROLE}, homeIdParamName = "homeId")
    public Response deleteMeal(@PathParam("idHome") UUID idHome, @PathParam("idMeal") UUID idMeal) {
        mealService.deleteMeal(idHome, idMeal);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
