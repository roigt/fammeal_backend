package org.univartois.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.univartois.entity.id.RecipesIngredientsId;

@Entity
@Table(name = "recipes_ingredients")
@AllArgsConstructor
@Builder
public class RecipesIngredientsEntity extends PanacheEntityBase {

    @EmbeddedId
    private RecipesIngredientsId id;

    @ManyToOne
    @MapsId("id_recipe")
    @JoinColumn(name = "id_recipe", nullable = false)
    public RecipeEntity recipe;

    @ManyToOne
    @MapsId("id_ingredient")
    @JoinColumn(name = "id_ingredient", nullable = false)
    public IngredientEntity ingredient;

    @Column(name = "quantity_need", nullable = false)
    public float quantityNeed;

    public RecipesIngredientsEntity() {}

    public RecipesIngredientsEntity(RecipeEntity recipe, IngredientEntity ingredient, float quantityNeed) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantityNeed = quantityNeed;
        this.id = new RecipesIngredientsId(recipe.getIdRecipe(), ingredient.getIdIngredient());
    }


}
