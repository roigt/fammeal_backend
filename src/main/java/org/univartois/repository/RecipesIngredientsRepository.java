package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.id.RecipesIngredientsId;
import org.univartois.entity.RecipesIngredientsEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RecipesIngredientsRepository implements PanacheRepositoryBase<RecipesIngredientsEntity, RecipesIngredientsId> {
    public List<RecipesIngredientsEntity> findByRecipeId(UUID recipeId) {
        return find("recipe.idRecipe = ?1", recipeId).list();
    }

}
