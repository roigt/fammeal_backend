package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.univartois.entity.MealEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MealRepository implements PanacheRepositoryBase<MealEntity, UUID> {

    /**
     * Trouver les repas associés à une maison spécifique
     *
     * @param idhome l'entité HomeEntity de la maison
     * @return Liste des MealEntity associés à la maison
     */
    public List<MealEntity> findByHome(UUID idhome) {
        return find("home.id =?1 ", idhome).list();
    }

    /**
     * Trouver les repas associés à une maison spécifique via l'ID de la maison
     *
     * @param idHome l'UUID de la maison
     * @return Liste des MealEntity associés à la maison
     */
    public List<MealEntity> findByHomeId(UUID idHome) {
        return find("home.id", idHome).list();
    }

    public MealEntity findByIdRecipe(UUID recipeId) {
        return find("recipe.idRecipe", recipeId).firstResult();
    }

    public MealEntity findByIdHomeDateAndLunch(UUID idHome, LocalDate date, Boolean lunch) {
      return find("home.id=?1 AND mealDate=?2 AND mealLunch=?3",idHome,date,lunch).firstResult();
    }

    /**
     * Trouver un repas d'une maison précis
     *
     * @param idHome
     * @param mealId
     * @return
     */
    public MealEntity findByHomeAndIdMeal(UUID idHome, UUID mealId) {
        return find("home.id =?1 and idMeal =?2", idHome, mealId).firstResult();

    }

    @Transactional
    public List<MealEntity> findFromDateToDate(UUID idHome, LocalDate from, LocalDate to) {
        return find("home.id = ?1 AND mealDate BETWEEN ?2 AND ?3 ORDER BY mealDate", idHome, from, to).list();
    }


}
