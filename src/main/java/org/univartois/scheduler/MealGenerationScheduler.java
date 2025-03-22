package org.univartois.scheduler;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.univartois.entity.*;
import org.univartois.repository.*;
import org.univartois.service.AiService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class MealGenerationScheduler {

    @Inject
    HomeRepository homeRepository;

    @Inject
    RecipeRepository recipeRepository;

    @Inject
    MealRepository mealRepository;

    @Inject
    AiService aiService;

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    AllergyRepository allergyRepository;

    @Inject
    UserRepository userRepository;


    @Transactional
    @Scheduled(cron = "{generate.meal.cron}")
    public void generateMeals(ScheduledExecution execution) {
        Instant fireTime = execution.getScheduledFireTime();
        ZonedDateTime zonedDateTime = fireTime.atZone(ZoneId.systemDefault());
        LocalDate day = zonedDateTime.toLocalDate();

        for (HomeEntity home : homeRepository.findWithAutomaticMealGeneration()) {
            Set<AllergyEntity> allergies = home.getAllergies();
            boolean isVegan = home.isVegetarian();

            List<RecipeEntity> recipes = recipeRepository.findByVegetarianAndNotInAllergiesAndPublic(isVegan, allergies, true);
            if (home.isLunchAutomaticGeneration()) {
                MealEntity meal = mealRepository.findByIdHomeDateAndLunch(home.getId(), day, true);
                if (meal == null || meal.getRecipe() == null) {
                    generateMealForHome(meal, home, true, day, recipes, allergies, isVegan);
                }
            }

            if (home.isDinerAutomaticGeneration()) {
                MealEntity meal = mealRepository.findByIdHomeDateAndLunch(home.getId(), day, false);
                if (meal == null || meal.getRecipe() == null) {
                    generateMealForHome(meal, home, false, day, recipes, allergies, isVegan);
                }
            }
        }

    }

    public void generateMealForHome(MealEntity meal, HomeEntity home, boolean isLunch, LocalDate mealDay, List<RecipeEntity> recipes, Set<AllergyEntity> allergies, boolean isVegan) {
        Random random = new Random();
        RecipeEntity recipe;
        if (recipes.isEmpty()) {
            recipe = generateRecipe(allergies, isVegan);
        } else {
            recipe = recipes.get(random.nextInt(recipes.size()));
        }

        if (meal == null) {
            meal = MealEntity.builder().home(home).mealDate(mealDay).mealLunch(isLunch).recipe(recipe).build();
        } else if (meal.getRecipe() == null) {
            meal.setRecipe(recipe);
        }

        mealRepository.persist(meal);
    }

    public RecipeEntity generateRecipe(Set<AllergyEntity> allergies, boolean isVegan) {
        Set<IngredientEntity> ingredients = new HashSet<>(ingredientRepository.findAllIngredients());
        UserEntity admin = userRepository.findByEmail("admin@fammeal.com").orElseThrow(() -> new IllegalStateException("Admin user not found"));

        Set<String> allergiesNames = allergies.stream().map(AllergyEntity::getName).collect(Collectors.toSet());
        Set<String> ingredientsNames = ingredients.stream().map(IngredientEntity::getIngredientName).collect(Collectors.toSet());
        AiService.RecipeAiDto recipeAiDto = aiService.generateRecipe(allergiesNames, ingredientsNames, isVegan);
        RecipeEntity recipe = mapToRecipeEntity(recipeAiDto, admin);
        log.info("Generated recipe: {}", recipeAiDto);

        recipe.setRecipesIngredients(new ArrayList<>());

        for (AiService.IngredientAiDto ingredientAiDto : recipeAiDto.ingredients()) {
            IngredientEntity ingredient = ingredientRepository.findByIngredientName(ingredientAiDto.ingredientName()).orElseGet(() -> {
                IngredientEntity newIngredient = new IngredientEntity();
                newIngredient.setIngredientName(ingredientAiDto.ingredientName());
                newIngredient.setIngredientIsVegan(ingredientAiDto.vegan());
                newIngredient.setIdUnit(ingredientAiDto.unit());
                newIngredient.setNbDayBeforeExpiration(ingredientAiDto.nbDayBeforeExpiration());
                newIngredient.setAllergies(new HashSet<>());
                newIngredient.setRecipesIngredients(new ArrayList<>());
                return newIngredient;
            });

            RecipesIngredientsEntity recipeIngredient = new RecipesIngredientsEntity(recipe, ingredient, ingredientAiDto.quantityNeed());
            ingredient.getRecipesIngredients().add(recipeIngredient);
            recipe.getRecipesIngredients().add(recipeIngredient);
            if (ingredient.getAllergies().stream().noneMatch((a) -> a.getName().equalsIgnoreCase(ingredientAiDto.ingredientName()))) {
                AllergyEntity allergy = new AllergyEntity();
                allergy.setId(null);
                allergy.setName(ingredientAiDto.ingredientName());
                allergyRepository.persist(allergy);
                ingredient.getAllergies().add(allergy);
            }

            ingredientRepository.persist(ingredient);
            recipeIngredient.persist();
        }

        recipeRepository.persist(recipe);
        return recipe;
    }

    private RecipeEntity mapToRecipeEntity(AiService.RecipeAiDto recipeAiDto, UserEntity admin) {
        return RecipeEntity.builder()
                .recipeName(recipeAiDto.recipeName())
                .recipeNbCovers(recipeAiDto.recipeNbCovers())
                .recipeInstructions(recipeAiDto.recipeInstructions())
                .recipeImageLink(recipeAiDto.recipeImageLink())
                .recipePublic(false)
                .prepTimeMinutes(recipeAiDto.prepTimeMinutes())
                .cookTimeMinutes(recipeAiDto.cookTimeMinutes())
                .user(admin)
                .recipeLunchBox(recipeAiDto.recipeLunchBox())
                .recipeVideoLink(null)
                .build();
    }
}
