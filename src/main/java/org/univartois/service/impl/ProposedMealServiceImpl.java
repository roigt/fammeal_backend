package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.univartois.dto.request.ProposedMealRequestDto;

import org.univartois.dto.response.MealProposalsByDateResponse;
import org.univartois.dto.response.ProposedMealResponseDto;

import org.univartois.entity.*;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.ProposedMealMapper;
import org.univartois.repository.*;
import org.univartois.service.ProposedMealService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@ApplicationScoped
public class ProposedMealServiceImpl implements ProposedMealService {

    @Inject
    ProposedMealRepository proposedMealRepository;

    @Inject
    ProposedMealMapper proposedMealMapper;

    @Inject
    RecipeRepository recipeRepository;

    @Inject
    MealRepository mealRepository;

    @Inject
    UserRepository userRepository;
    @Inject
    HomeRepository homeRepository;

    /**
     * Afficher tous les repas proposés
     * @return
     */
    @Override
    public List<ProposedMealResponseDto> getAllProposedMeals(UUID homeId) {
        HomeEntity  home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));
        return proposedMealMapper.toResponseDtoList(proposedMealRepository.findByAllPropositions());
    }

    /**
     * Récupérer les propositions de repas par ID de repas
     * @param mealId
     * @return Liste des ProposedMealResponseDto
     */
    @Override
    public List<ProposedMealResponseDto> getProposedMealsByMealId(UUID homeId,UUID mealId) {
        HomeEntity  home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        List<ProposedMealEntity> proposedMeals = proposedMealRepository.findByMealId(mealId);
        if (proposedMeals.isEmpty()) {
            throw new RuntimeException("No proposed meals found for this meal");
        }
        return proposedMealMapper.toResponseDtoList(proposedMeals);
    }


    /**
     * Ajouter une proposition de repas
     * @param proposedMealRequestDto
     * @return ProposedMealResponseDto
     */
    @Transactional
    @Override
    public ProposedMealResponseDto proposeMeal(UUID homeId,ProposedMealRequestDto proposedMealRequestDto) {

        HomeEntity  home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        RecipeEntity recipe = recipeRepository.findById(proposedMealRequestDto.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        MealEntity meal = mealRepository.findById(proposedMealRequestDto.getMealId())
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        UserEntity proposer = userRepository.findById(proposedMealRequestDto.getProposerId())
                .orElseThrow(() -> new RuntimeException("User not found"));



        ProposedMealEntity proposedMealEntity = proposedMealMapper.toEntity(proposedMealRequestDto);

        proposedMealRepository.persist(proposedMealEntity);

        return proposedMealMapper.toResponseDto(proposedMealEntity);
    }



    /**
     * Mettre à jour une proposition de repas
     * @param recipeId
     * @param mealId
     * @param proposerId
     * @param proposedMealRequestDto
     * @return ProposedMealResponseDto
     */
    @Transactional
    @Override
    public ProposedMealResponseDto updateProposedMeal(UUID recipeId, UUID mealId, UUID proposerId, ProposedMealRequestDto proposedMealRequestDto) {
        ProposedMealEntity proposedMealEntity = proposedMealRepository.findById(recipeId, mealId, proposerId)
                .orElseThrow(() -> new ResourceNotFoundException("Proposed meal not found"));


        RecipeEntity newRecipe = recipeRepository.findById(proposedMealRequestDto.getRecipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        proposedMealEntity.setRecipe(newRecipe);

        return proposedMealMapper.toResponseDto(proposedMealEntity);
    }

    @Transactional
    @Override
    public MealProposalsByDateResponse searchMealByDate(UUID homeId, LocalDate date){
        //on recupère la liste complet des meals proposés
        List<ProposedMealEntity> proposedMealEntities = proposedMealRepository.getProposedMealsByDate(date);


        //on regroupe les meals selon le type lunch ou dinner
        Map<String, List<ProposedMealEntity>> groupedByMealType = proposedMealEntities.stream()
                .collect(Collectors.groupingBy(proposal -> {
                    MealEntity meal = proposal.getMeal();
                    return meal.isMealLunch() ? "lunch" : "dinner";
                }));

        //on creer l objet de la reponse
        MealProposalsByDateResponse proposalsByDate = new MealProposalsByDateResponse();

        groupedByMealType.forEach((mealType, proposals) -> {

            ProposedMealResponseDto proposedMealResponseDto = new ProposedMealResponseDto();


            MealProposalsByDateResponse.mealsPrototype mealsPrototype = new MealProposalsByDateResponse.mealsPrototype();
            //on recupère le nombre de proposition selon le type
            mealsPrototype.setNbPropositions(proposals.size());

            proposals.forEach(proposal -> {
                ProposedMealResponseDto.RecipeDto  recipeDto = new ProposedMealResponseDto.RecipeDto(proposal.getRecipe().getIdRecipe(), proposal.getRecipe().getRecipeName(),
                        proposal.getProposer().getProfilePictureUrl());
                //on construit chaque meals en fonction du type de meal lunch ou dinner
                proposedMealResponseDto.setRecipeId(proposal.getRecipe().getIdRecipe());
                proposedMealResponseDto.setMealId(proposal.getMeal().getIdMeal());
                proposedMealResponseDto.setProposerId(proposal.getProposer().getId());
                proposedMealResponseDto.setRecipe(recipeDto);

                //on ajoute le meal creer au prototype selon le lunch ou le dinner
                mealsPrototype.getMealList().add(proposedMealResponseDto);

                 //si le repas es de type lunch on le met dans le lunch de la reponse
                if(Objects.equals(mealType, "lunch")){
                    proposalsByDate.setLunch(mealsPrototype);
                }else{// si non le met dans le dinner de la reponse
                    proposalsByDate.setDiner(mealsPrototype);
                }
            });
        });


    return proposalsByDate;

    }

    /**
     * Supprimer une proposition de repas
     * @param recipeId
     * @param mealId
     * @param proposerId
     */
    @Transactional
    @Override
    public void deleteProposedMeal(UUID recipeId, UUID mealId, UUID proposerId) {
        proposedMealRepository.deleteById(recipeId, mealId, proposerId);
    }
}
