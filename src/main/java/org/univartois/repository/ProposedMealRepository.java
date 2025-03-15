package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.ProposedMealEntity;
import org.univartois.entity.ProposedMealEntity.ProposedMealId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProposedMealRepository implements PanacheRepositoryBase<ProposedMealEntity, ProposedMealId> {


    public List<ProposedMealEntity> findByAllPropositions() {
        return listAll();
    }


    /**
     * Trouver les repas proposés pour un repas spécifique.
     *
     * @param mealId l'UUID du repas
     * @return Liste des ProposedMealEntity associés au repas
     */
    public List<ProposedMealEntity> findByMealId(UUID mealId) {
        return find("meal.idMeal = ?1", mealId).list();
    }

    /**
     * Trouver les repas proposés pour une recette spécifique.
     *
     * @param recipeId l'UUID de la recette
     * @return Liste des ProposedMealEntity associés à la recette
     */
    public List<ProposedMealEntity> findByRecipe(UUID recipeId) {
        return find("recipe.id = ?1", recipeId).list();
    }

    /**
     * Trouver les repas proposés par un utilisateur spécifique.
     *
     * @param proposerId l'UUID de l'utilisateur proposant le repas
     * @return Liste des ProposedMealEntity proposés par l'utilisateur
     */
    public List<ProposedMealEntity> findByProposer(UUID proposerId) {
        return find("proposer.id = ?1", proposerId).list();
    }

    /**
     * Trouver une proposition spécifique par son ID composite.
     *
     * @param recipeId   l'UUID de la recette
     * @param mealId     l'UUID du repas
     * @param proposerId l'UUID du proposeur
     * @return Optional<ProposedMealEntity>
     */
    public Optional<ProposedMealEntity> findById(UUID recipeId, UUID mealId, UUID proposerId) {
        return find("recipe.id = ?1 AND meal.id = ?2 AND proposer.id = ?3", recipeId, mealId, proposerId).firstResultOptional();
    }

    public ProposedMealEntity findByMealIdAndProposerId(UUID mealId, UUID proposerId) {
        return find(" meal.id = ?1  AND proposer.id=?2", mealId, proposerId).firstResult();

    }

    public List<ProposedMealEntity> findByProposerId(UUID proposerId) {
        return find(" proposer.id = ?1 ", proposerId).list();
    }
    public List<ProposedMealEntity> findByProposerIdAndMealDate(UUID proposerId,LocalDate date) {
        return find(" proposer.id = ?1 AND meal.mealDate= ?2 ", proposerId,date).list();
    }

    public List<ProposedMealEntity> getProposedMealsByDate(LocalDate date) {
        return find("meal.mealDate = ?1", date).list();
    }


    /**
     * Supprimer une proposition spécifique par son ID composite.
     *
     * @param recipeId   l'UUID de la recette
     * @param mealId     l'UUID du repas
     * @param proposerId l'UUID du proposeur
     */
    public void deleteById(UUID recipeId, UUID mealId, UUID proposerId) {
        delete("recipe.id = ?1 AND meal.id = ?2 AND proposer.id = ?3", recipeId, mealId, proposerId);
    }


}

