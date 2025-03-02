package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
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
public class MealServiceImpl implements MealService {

    @Inject
    HomeRepository homeRepository;

    @Inject
    MealRepository mealRepository;

    @Inject
    RecipeRepository recipeRepository;

    @Inject
    MealMapper mealMapper;

    @Inject
    JsonWebToken jwt;
    @Inject
    ProposedMealServiceImpl proposedMealServiceImpl;
    @Inject
    ProposedMealRepository proposedMealRepository;
    @Inject
    UserRepository userRepository;


    /**
     * Récupérer le planning des repas pour une maison donnée
     * @param homeId
     * @return Liste des repas
     */
    @Override
    public List<MealResponseDto> getMealsByHome(UUID homeId) {
        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        List<MealEntity> meals = mealRepository.findByHome(homeId);

        return mealMapper.toResponseDtoList(meals);
    }

    @Override
    public MealResponseDto getMealsByHomeAndIdMeal(UUID homeId, UUID mealId) {
        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));


        MealEntity meals = mealRepository.findByHomeAndIdMeal(homeId,mealId);

        return mealMapper.toResponseDto(meals);
    }

    @Override
    public MealResponseFromDateToDto getMealDateTo(UUID idHome, LocalDate from, LocalDate to){
     List<MealEntity> mealEntities = mealRepository.findFromDateToDate(idHome,from,to);

        Map<String, List<MealEntity>> groupedByMealType = mealEntities.stream()
                .collect(Collectors.groupingBy(meal -> meal.isMealLunch() ? "lunch" : "dinner"));


        MealResponseFromDateToDto mealResponseFromTo = new MealResponseFromDateToDto();
        Map<LocalDate, MealResponseFromDateToDto.DailyMealsDto> mealFromTo = new HashMap<>();

        MealResponseFromDateToDto.DailyMealsDto day= new MealResponseFromDateToDto.DailyMealsDto();


        groupedByMealType.forEach((mealType, meals) -> {
            MealResponseFromDateToDto.MealDto  mealDto = new MealResponseFromDateToDto.MealDto();
            System.out.println("Meal Type: " + mealType);
            meals.forEach(meal -> {


                mealDto.setId_recipe(meal.getRecipe().getIdRecipe());
                mealDto.setName(meal.getRecipe().getRecipeName());
                mealDto.setImage_url(meal.getRecipe().getRecipeImageLink());

                if(mealType.equals("lunch")){
                  day.getLunch().add(mealDto);
                }else{
                    day.getDiner().add(mealDto);
                }



            });

            mealFromTo.put(meals.get(0).getMealDate(), day);



        });

      mealResponseFromTo.setMeals(mealFromTo);



             return mealResponseFromTo;
//        return mealMapper.toResponseDtoList(mealEntities);
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
        Optional<UserEntity> user = userRepository.findById(userId);


        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        RecipeEntity recipe = recipeRepository.findById(mealRequestDto.getIdRecipe())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));


        if (mealRequestDto.getMealDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Meal date is before today-> mealDate ");
        }

        //gestion du cas ou l utilisateur a deja proposer un repas pour le dejeuner ou le dinner
        if(proposedMealRepository.findByProposerId(userId)!=null){
            //recupère la liste des propositions de l utilisateur
           List<ProposedMealEntity> proposedMeal = proposedMealRepository.findByProposerId(userId);

           for(ProposedMealEntity proposedMealEntity : proposedMeal){

               if(proposedMealEntity.getMeal().isMealLunch() && mealRequestDto.isMealLunch()){
                   throw new RuntimeException("Vous avez déja proposer un repas pour le dejeuner . ");
               }else if(!proposedMealEntity.getMeal().isMealLunch() && !mealRequestDto.isMealLunch()){
                   throw new RuntimeException("vous avez déja proposer un repas pour le dinner. ");
               }
           }


        }

        MealEntity mealEntity = mealMapper.toEntity(mealRequestDto);
        mealEntity.setHome(home);
        mealEntity.setRecipe(recipe);

        mealRepository.persist(mealEntity);

        //rajouter la proposition de l utilisateur dans proposedMeal
        ProposedMealEntity proposedMeal = new ProposedMealEntity(recipe,mealEntity,user.orElse(null));
        proposedMealRepository.persist(proposedMeal);

        return mealMapper.toResponseDto(mealEntity);
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
        Optional<UserEntity> user = userRepository.findById(userId);


        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        MealEntity mealEntity = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        RecipeEntity recipe = recipeRepository.findById(mealRequestDto.getIdRecipe())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));


        if (mealRequestDto.getMealDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Meal date is before today-> mealDate ");
        }

        if(proposedMealRepository.findByMealIdAndProposerId(mealId,userId).getMeal()!=null) {
            ProposedMealEntity proposeOld = proposedMealRepository.findByMealIdAndProposerId(mealId,userId);


            ProposedMealEntity proposedMeal = new ProposedMealEntity(
                 recipe,mealEntity,user.orElse(null)
            );

            if (proposeOld != null) {
                System.out.println("Existe deja dans proposed-meal");
                proposedMealRepository.delete(proposeOld);

            }
            proposedMealRepository.persist(proposedMeal);

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
        HomeEntity home = homeRepository.findById(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        MealEntity mealEntity = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        ProposedMealEntity proposeOld = proposedMealRepository.findByMealIdAndProposerId(mealId,userId);
        proposeOld.setMeal(null);
        proposedMealRepository.persist(proposeOld);
       // mealRepository.delete(mealEntity);
        // Safe delete  on met id_recipe à NULL sans supprimer le repas
        mealEntity.setRecipe(null);
       mealRepository.persist(mealEntity);
    }


}
