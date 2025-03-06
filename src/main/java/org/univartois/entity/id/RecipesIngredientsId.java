package org.univartois.entity.id;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class RecipesIngredientsId implements Serializable {
    private UUID id_recipe;
    private UUID id_ingredient;

    public RecipesIngredientsId() {}

    public RecipesIngredientsId(UUID id_recipe, UUID id_ingredient) {
        this.id_recipe = id_recipe;
        this.id_ingredient = id_ingredient;
    }

    public UUID getIdRecipe() {
        return id_recipe;
    }

    public UUID getIdIngredient() {
        return id_ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipesIngredientsId that = (RecipesIngredientsId) o;
        return Objects.equals(id_recipe, that.id_recipe) && Objects.equals(id_ingredient, that.id_ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_recipe, id_ingredient);
    }
}
