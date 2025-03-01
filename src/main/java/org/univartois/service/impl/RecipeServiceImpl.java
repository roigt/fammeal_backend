package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Request;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.univartois.dto.request.RecipeRequestDto;
import org.univartois.dto.response.RecipeResponseDto;
import org.univartois.entity.IngredientEntity;
import org.univartois.entity.RecipeEntity;
import org.univartois.entity.RecipesIngredientsEntity;
import org.univartois.entity.UserEntity;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.RecipeMapper;
import org.univartois.repository.IngredientRepository;
import org.univartois.repository.RecipeRepository;
import org.univartois.repository.RecipesIngredientsRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.RecipeService;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeServiceImpl implements RecipeService {

    @Inject
    RecipeRepository recipeRepository;

    @Inject
    RecipeMapper recipeMapper;

    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;
    @Inject
    IngredientRepository ingredientRepository;
    @Inject
    RecipesIngredientsRepository recipesIngredientsRepository;
    @Inject
    Request request;

    /**
     * Créer une nouvelle recette
     * @param createRecipeRequestDto
     * @return
     */
    @Transactional
    @Override
    public RecipeResponseDto createRecipe(RecipeRequestDto createRecipeRequestDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        UserEntity creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RecipeEntity recipeEntity = new RecipeEntity();


        recipeEntity.setRecipeImageLink(createRecipeRequestDto.getRecipeImageLink());
        recipeEntity.setRecipeName(createRecipeRequestDto.getRecipeName());
        addDataInRecipeEntity(createRecipeRequestDto, recipeEntity);



        createRecipeRequestDto.getIngredients().forEach((ingredientId, quantity) -> {
            // Trouver l'ingrédient à partir de son ID
            IngredientEntity ingredientEntity = ingredientRepository.findByIdE(ingredientId);

            // Créer un RecipesIngredientsEntity en utilisant la recette et l'ingrédient trouvé
            RecipesIngredientsEntity recipesIngredientsEntity = new RecipesIngredientsEntity(
                    recipeEntity,  // Recette
                    ingredientEntity,  // Ingrédient trouvé
                    quantity  // Quantité de l'ingrédient
            );

            // Ajouter l'entité ingredient  à la recette
            recipeEntity.getRecipesIngredients().add(recipesIngredientsEntity);


            recipesIngredientsRepository.persist(recipesIngredientsEntity);
        });

        recipeEntity.setUser(creator);

        recipeRepository.persist(recipeEntity);


        return recipeMapper.toResponseDto(recipeEntity);
    }

    private void addDataInRecipeEntity(RecipeRequestDto createRecipeRequestDto, RecipeEntity recipeEntity) {
        recipeEntity.setPrepTimeMinutes(createRecipeRequestDto.getPrepTimeMinutes());
        recipeEntity.setRecipeVideoLink(createRecipeRequestDto.getRecipeVideoLink());
        recipeEntity.setRecipeInstructions(createRecipeRequestDto.getRecipeInstructions());
        recipeEntity.setRecipePublic(createRecipeRequestDto.isRecipePublic());
        recipeEntity.setRecipeLunchBox(createRecipeRequestDto.isRecipeLunchBox());
        recipeEntity.setRecipeNbCovers(createRecipeRequestDto.getRecipeNbCovers());
        recipeEntity.setCookTimeMinutes(createRecipeRequestDto.getCookTimeMinutes());
    }


    /**
     * Récupérer une recette par son ID
     * @param recipeId
     * @return
     */
    @Override
    public RecipeResponseDto getRecipeById(UUID recipeId) {

        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        List<RecipesIngredientsEntity> recipeIngredients = recipesIngredientsRepository.findByRecipeId(recipeId);
        RecipeResponseDto recipeResponseDto = recipeMapper.toResponseDto(recipeEntity);

        for (RecipesIngredientsEntity recipesIngredient : recipeIngredients) {
          //  recipeResponseDto.getIdsIngredients().add(recipesIngredient.ingredient.getIdIngredient());
            RecipeResponseDto.Ingredients in = new RecipeResponseDto.Ingredients(recipesIngredient.ingredient.getIdIngredient(),
                    recipesIngredient.ingredient.getIngredientName(), recipesIngredient.quantityNeed, recipesIngredient.ingredient.isIngredientIsVegan(),
                    recipesIngredient.ingredient.getIdUnit());

            recipeResponseDto.getIngredients().add(in);

        }

        return  recipeResponseDto;
    }

    /**
     * Récupérer toutes les recettes
     * @return
     */
    @Override
    public List<RecipeResponseDto> getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeRepository.listAll();
        if (recipeEntities.isEmpty()) throw new ResourceNotFoundException("Recipes not found");

        //on recupère la liste de toutes les recette dans la table recette
        List<RecipeResponseDto> allRecipes = recipeMapper.toResponseDtoList(recipeEntities);


        return getRecipeResponseDtos(allRecipes);
    }


    /**
     * Recupérer la liste de toutes les recettes publiques
     * @return
     */
    @Override
    public List<RecipeResponseDto> getAllPublicRecipes(){
        List<RecipeEntity> recipeEntitiesPublic = recipeRepository.findPublicRecipes();
        if (recipeEntitiesPublic.isEmpty()) throw new ResourceNotFoundException("Recipes public not found");
        return recipeMapper.toResponseDtoList(recipeEntitiesPublic);
    }



    /**
     * Mettre à jour une recette existante
     * @param recipeId
     * @param recipeRequestDto
     * @return
     */
    @Transactional
    @Override
    public RecipeResponseDto updateRecipe(UUID recipeId, RecipeRequestDto recipeRequestDto) {
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        recipeEntity.setRecipeName(recipeRequestDto.getRecipeName());
        recipeEntity.setRecipeImageLink(recipeRequestDto.getRecipeImageLink());
        addDataInRecipeEntity(recipeRequestDto, recipeEntity);


        //  List<UUID> recipesIngredientsRequest = recipeRequestDto.getIdsIngredients();


        List<RecipesIngredientsEntity> recipeIngredients = recipesIngredientsRepository.findByRecipeId(recipeId);


        //on supprime toutes les lignes de ces ingredient de la recipe dans recipeIngredient
        for (RecipesIngredientsEntity recipesIngredient : recipeIngredients){
            recipesIngredientsRepository.delete(recipesIngredient);
        }

        recipeRequestDto.getIngredients().forEach((ingredientId, quantity) -> {
            // Trouver l'ingrédient à partir de son ID
            IngredientEntity ingredientEntity = ingredientRepository.findByIdE(ingredientId);

            // Créer un RecipesIngredientsEntity en utilisant la recette et l'ingrédient trouvé
            RecipesIngredientsEntity recipesIngredientsEntity = new RecipesIngredientsEntity(
                    recipeEntity,  // Recette
                    ingredientEntity,  // Ingrédient trouvé
                    quantity  // Quantité de l'ingrédient
            );

            // Ajouter l'entité à la recette
            recipeEntity.getRecipesIngredients().add(recipesIngredientsEntity);

            // Sauvegarder dans le repository
            recipesIngredientsRepository.persist(recipesIngredientsEntity);
        });




        RecipeResponseDto recipeResponseDto = recipeMapper.toResponseDto(recipeEntity);

        recipeIngredientsList(recipeResponseDto, recipeId);


        return  recipeResponseDto;

    }




    /**
     * Supprimer une recette
     * @param recipeId
     */
    @Transactional
    @Override
    public void deleteRecipe(UUID recipeId) {
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        //  soft delete
         recipeEntity.setUser(null);
        recipeEntity.setRecipePublic(false);
    }

    @Override
    public List<RecipeResponseDto> searchRecipes(List<String> keywords, List<String> ingredientIds, Boolean vegetarian, Integer covers, Boolean lunchBox) {

        List<RecipeResponseDto> recipeResponseDto = recipeRepository.searchRecipes(keywords, ingredientIds, vegetarian, covers, lunchBox)
                .stream()
                .map(recipeMapper::toResponseDto)
                .toList();

        return getRecipeResponseDtos(recipeResponseDto);

    }

    /**
     * Parcourir chaque recette de la liste de recette de la reponse et leur ajouter
     * chacun la liste des infos de chaque ingredients
     * @param recipeResponseDto
     * @return
     */
    private List<RecipeResponseDto> getRecipeResponseDtos(List<RecipeResponseDto> recipeResponseDto) {
        for (RecipeResponseDto recipe : recipeResponseDto) {
            //on recupère id de chaque recette
            UUID recipeId = recipe.getIdRecipe();
            //on recupère les id des ingrédients liés à chaque recette dans la table recipeIngredient
            recipeIngredientsList(recipe, recipeId);

        }

        return recipeResponseDto;
    }

    /**
     * fonction qui permet de creer la liste des infos sur les ingredients de chaque recette
     * @param recipe
     * @param recipeId
     */
    private void recipeIngredientsList(RecipeResponseDto recipe, UUID recipeId) {
        List<RecipesIngredientsEntity> recipesIngredients = recipesIngredientsRepository.findByRecipeId(recipeId);

        for(RecipesIngredientsEntity recipesIngredient : recipesIngredients) {
            //pour chaque recette on ajoute les ingredients qu'il contient depuis les id ingredient correspondant dans
            //la table recipeIngredient
            //   recipe.getIdsIngredients().add(recipesIngredient.ingredient.getIdIngredient());
            //on creer la liste des infos des ingredients pour chaque recette
            RecipeResponseDto.Ingredients ingredients = new RecipeResponseDto.Ingredients(recipesIngredient.ingredient.getIdIngredient(),
                    recipesIngredient.ingredient.getIngredientName(),recipesIngredient.quantityNeed, recipesIngredient.ingredient.isIngredientIsVegan(),
                    recipesIngredient.ingredient.getIdUnit());

            recipe.getIngredients().add(ingredients);
        }
    }
}
