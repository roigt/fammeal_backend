package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import org.univartois.entity.RecipeEntity;
import org.univartois.entity.RecipesIngredientsEntity;
import org.univartois.entity.UserEntity;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeRepository implements PanacheRepositoryBase<RecipeEntity, UUID> {


    public RecipeEntity findByIdRecipe(UUID id) {
        return find("idRecipe= ?1", id).firstResult();
    }
    

    /**
     * Recherche toutes les recettes d'un créateur
     * @param creatorId
     * @return
     */
    public List<RecipeEntity> findByCreatorId(UUID creatorId) {
        return list("user.id = ?1", creatorId);
    }

    /**
     * Recherche toutes les recettes publiques
     * @return
     */
    public List<RecipeEntity> findPublicRecipes() {
        return list("recipePublic = true");
    }

    /**
     *  Recherche toutes les recettes adaptées à une boîte à lunch
     * @return
     */
    public List<RecipeEntity> findLunchBoxRecipes() {
        return list("recipeLunchBox = true");
    }

    /**
     * Recherche une recette par son nom ou un terme clé dans les instructions
     * @param keyword
     * @return
     */
    public List<RecipeEntity> findByInstructionKeyword(String keyword) {
        return list("recipeInstructions like ?1", "%" + keyword + "%");
    }


    public List<RecipeEntity> searchRecipes(List<String> keywords, List<String> ingredientIds, Boolean vegetarian,
                                            Integer covers, Boolean lunchBox) {


        List<String> allKeywords = new ArrayList<>();
        List<String> allIngredientIds = new ArrayList<>();



        for (String keyword : keywords) {
            String[] splitKeywords = keyword.split(",");
            allKeywords.addAll(Arrays.asList(splitKeywords));
        }

        for (String ingredientId : ingredientIds) {
            String[] splitIngredientId = ingredientId.split(",");
            allIngredientIds.addAll(Arrays.asList(splitIngredientId));
        }



        //recupérer toutes les recettes dans l alias r
        StringBuilder query = new StringBuilder("SELECT r FROM RecipeEntity r WHERE 1=1");
        Map<String, Object> params = new HashMap<>();
        query.append(" AND (");
        if (!allKeywords.isEmpty()) {

           for(int i= 0; i < allKeywords.size(); i++) {
              query.append("( LOWER(r.recipeName) LIKE :keyword").append(i).append(" OR LOWER(r.recipeInstructions) LIKE :keyword").append(i).append(")");

              params.put("keyword" + i, "%" + allKeywords.get(i).toLowerCase() + "%");
               if (i < allKeywords.size() - 1) {
                    query.append(" OR ") ;
                }


           }
            query.append(")");
        }

        if (!allIngredientIds.isEmpty()) {
            query.append(" AND EXISTS (");
            query.append("    SELECT 1 ");
            query.append("    FROM RecipesIngredientsEntity ri ");
            query.append("    WHERE ri.recipe.idRecipe = r.idRecipe ");
            query.append("    AND ri.ingredient.idIngredient IN :ingredientIds ");
            query.append("    GROUP BY ri.recipe.idRecipe ");
            query.append("    HAVING COUNT(DISTINCT ri.ingredient.idIngredient) = :ingredientCount");
            query.append(")");

            params.put("ingredientIds", allIngredientIds);
            params.put("ingredientCount", allIngredientIds.size());
        }



        if (vegetarian != null) {

            if (vegetarian) {
                // Si vegetarian on exclu toutes les recettes qui ne contiene pas d' ingredients non vegan
                query.append(" AND NOT EXISTS (");
                query.append("   SELECT 1 ");
                query.append("   FROM RecipesIngredientsEntity ri ");
                query.append("   WHERE ri.recipe.idRecipe = r.idRecipe ");
                query.append("   AND ri.ingredient.ingredientIsVegan = false ");
                query.append(" )");
            } else {
                //Si il y a parmi les ingredients au moins 1 ingredient non vegan alors cette recette n est pas vegan
                query.append(" AND EXISTS (");
                query.append("   SELECT 1 ");
                query.append("   FROM RecipesIngredientsEntity ri ");
                query.append("   WHERE ri.recipe.idRecipe = r.idRecipe ");
                query.append("   AND ri.ingredient.ingredientIsVegan = false ");
                query.append(" )");
            }

        }

        if (covers != null) {
            query.append(" AND r.recipeNbCovers = :covers");
            params.put("covers", covers);
        }

        if (lunchBox != null) {
            query.append(" AND r.recipeLunchBox = :lunchBox");
            params.put("lunchBox", lunchBox);
        }

//        if (allergyIds != null && !allergyIds.isEmpty()) {
//            query += " AND NOT EXISTS (SELECT 1 FROM RecipeIngredientEntity ri JOIN AllergyEntity a ON ri.ingredient.id = a.ingredient.id WHERE ri.recipe.id = r.id AND a.id IN :allergyIds)";
//            params.put("allergyIds", allergyIds);
//        }

        TypedQuery<RecipeEntity> typedQuery = getEntityManager().createQuery(query.toString(), RecipeEntity.class);
        params.forEach(typedQuery::setParameter);

        return typedQuery.getResultList();
    }

}
