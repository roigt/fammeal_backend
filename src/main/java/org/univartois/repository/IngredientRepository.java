package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import org.univartois.entity.IngredientEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class IngredientRepository implements PanacheRepositoryBase<IngredientEntity, UUID> {
    

    public IngredientEntity findByIdE(UUID id) {
        return find("idIngredient = ?1", id).firstResult();
    }

    public Optional<IngredientEntity> findByName(String name) {
        return find("ingredientName = ?1", name).firstResultOptional();
    }


    public List<IngredientEntity> findAllIngredients() {
        return listAll();
    }


    public List<IngredientEntity> findByVeganStatus(boolean isVegan) {
        return list("ingredientIsVegan = ?1", isVegan);
    }


    public List<IngredientEntity> findByExpirationDateBefore(int days) {
        return list("nbDayBeforeExpiration < ?1", days);
    }

    public Optional<IngredientEntity> findByIngredientName(@NotBlank(message = "Le nom de l'ingrédient ne doit pas être vide") String ingredientName) {
        return find("ingredientName", ingredientName).firstResultOptional();
    }
}
