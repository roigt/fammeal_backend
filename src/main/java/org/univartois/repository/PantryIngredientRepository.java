package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.HomeEntity;
import org.univartois.entity.IngredientEntity;
import org.univartois.entity.PantryIngredientEntity;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PantryIngredientRepository implements PanacheRepository<PantryIngredientEntity> {


    /**
     * Recupérer la liste des ingrédients dans le garde-manger pour une maison donnée
     * @param homeId
     * @return
     */
    public List<PantryIngredientEntity> findByHome(UUID homeId) {
        return find("home.id = ?1", homeId).list();
    }



    public PantryIngredientEntity findByIdPantryIngredientAndHomeId(UUID idPantryIngredient,UUID homeId) {
        return find("idIngredientInPantry=?1 and home.id= ?2", idPantryIngredient,homeId).firstResult();
    }



    public Optional<PantryIngredientEntity> findByIdIngredientPantryAndHomeId(UUID idIngredient, UUID homeId) {
        return find("ingredient.idIngredient = ?1 and home.id = ?2", idIngredient, homeId).firstResultOptional();
    }

    /**
     * On vérifie si cet ingredient existe deja dans la maison
     * @param homeId
     * @return
     */
    public Optional<PantryIngredientEntity> findPantryIngredientByHomeId(UUID homeId) {
        return find("home.id = ?1", homeId).firstResultOptional();
    }


    public Optional<PantryIngredientEntity> findByIdPantryIngredient(UUID ingredientInPantryId) {
        return find("idIngredientInPantry=?1", ingredientInPantryId).firstResultOptional();
    }
}
