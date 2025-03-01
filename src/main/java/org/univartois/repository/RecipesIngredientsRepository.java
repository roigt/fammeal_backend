package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.RecipeEntity;
import org.univartois.entity.RecipesIngredientsEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RecipesIngredientsRepository implements PanacheRepository<RecipesIngredientsEntity> {
    public List<RecipesIngredientsEntity> findByRecipeId(UUID recipeId) {
        return find("recipe.idRecipe = ?1", recipeId).list();
    }

    public List<RecipesIngredientsEntity> findByIngredientIdAndRecipe(UUID ingredientId,UUID recipeId) {
        return find("ingredient.idIngredient  = ?1 and recipe.idRecipe =?2", ingredientId,recipeId).list();
    }
}
