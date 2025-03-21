package org.univartois.service.impl;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mapstruct.InheritConfiguration;
import org.univartois.dto.request.MealRequestDto;
import org.univartois.dto.response.MealResponseDto;
import org.univartois.dto.response.MealResponseFromDateToDto;
import org.univartois.entity.*;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.MealMapper;
import org.univartois.repository.*;
import org.univartois.service.MealService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@ApplicationScoped
public class MealServiceImpl implements MealService{

    @Inject
    MealRepository mealRepository;

    @Inject
    HomeRepository homeRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    ProposedMealRepository proposedMealRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    RecipeRepository recipeRepository;


    @Inject
    MealMapper mealMapper;


    @Override
    public MealResponseFromDateToDto getMealDateTo(UUID idHome, LocalDate from, LocalDate to) {
        List<MealEntity> mealEntities = mealRepository.findFromDateToDate(idHome, from, to);
        Map<LocalDate, MealResponseFromDateToDto.DailyMealsDto> mealFromTo = new HashMap<>();

        for (MealEntity meal : mealEntities) {
            if(meal.getRecipe()!=null){//si le meal n a pas ete supprimé c a d idRecipe a null
                LocalDate mealDate = meal.getMealDate();
                boolean isLunch = meal.isMealLunch();

                MealResponseFromDateToDto.DailyMealsDto dayMeals = mealFromTo.computeIfAbsent(mealDate,
                        date -> new MealResponseFromDateToDto.DailyMealsDto());


                MealResponseFromDateToDto.MealDto mealDto = new MealResponseFromDateToDto.MealDto();
                mealDto.setId_recipe(meal.getRecipe().getIdRecipe());
                mealDto.setName(meal.getRecipe().getRecipeName());
                mealDto.setImage_url(meal.getRecipe().getRecipeImageLink());
                mealDto.setIdMeal(meal.getIdMeal());


                if (isLunch) {
                    dayMeals.getLunch().add(mealDto);
                } else {
                    dayMeals.getDiner().add(mealDto);
                }
            }

        }

        MealResponseFromDateToDto mealResponseFromTo = new MealResponseFromDateToDto();
        mealResponseFromTo.setMeals(mealFromTo);

        return mealResponseFromTo;
    }



    /**
     * Planifie un repas pour une maison donnée
     * @param homeId
     * @param mealRequestDto
     * @return Le repas planifié
     */
    @Transactional
    @Override
    public MealResponseDto planMeal(UUID homeId, MealRequestDto mealRequestDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Optional<UserEntity> user = userRepository.findByIdOptional(userId);


        HomeEntity home = homeRepository.findByIdOptional(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        RecipeEntity recipe = recipeRepository.findByIdOptional(mealRequestDto.getIdRecipe())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));


        if (mealRequestDto.getMealDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Meal date is before today-> mealDate ");
        }

        //on verifie si le meal existe dans la table meal et on le crée si ce n'est pas le cas
        MealEntity meal = mealRepository.findByIdHomeDateAndLunch(homeId,mealRequestDto.getMealDate(),mealRequestDto.isMealLunch());
        if(meal==null){
            meal = mealMapper.toEntity(mealRequestDto);
            meal.setHome(home);
        }
        
        meal.setRecipe(recipe);
        mealRepository.persist(meal);
        return mealMapper.toResponseDto(meal);
    }

    /**
     * Met à jour un repas existant
     * @param homeId
     * @param mealId
     * @param mealRequestDto
     * @return Le repas mis à jour
     */
    @Transactional
    @Override
    public MealResponseDto updateMeal(UUID homeId, UUID mealId, MealRequestDto mealRequestDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Optional<UserEntity> user = userRepository.findByIdOptional(userId);


        HomeEntity home = homeRepository.findByIdOptional(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        MealEntity mealEntity = mealRepository.findByIdOptional(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        RecipeEntity recipe = recipeRepository.findByIdOptional(mealRequestDto.getIdRecipe())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));


        if (mealRequestDto.getMealDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Meal date is before today-> mealDate ");
        }


        // Mettre à jour les informations du repas
        MealEntity mealUpdate =mealMapper.toEntity(mealRequestDto);
        mealEntity.setMealDate(mealUpdate.getMealDate());
        mealEntity.setMealLunch(mealUpdate.isMealLunch());
        mealEntity.setRecipe(mealUpdate.getRecipe());
        mealRepository.persist(mealEntity);


        return mealMapper.toResponseDto(mealEntity);
    }

    /**
     * Supprime un repas planifié
     * @param homeId
     * @param mealId
     */
    @Transactional
    @Override
    public void deleteMeal(UUID homeId, UUID mealId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        HomeEntity home = homeRepository.findByIdOptional(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        MealEntity mealEntity = mealRepository.findByIdOptional(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        // Safe delete  on met id_recipe à NULL sans supprimer le repas
        mealEntity.setRecipe(null);

    }


}
