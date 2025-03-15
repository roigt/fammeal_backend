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


    public List<RecipeEntity> searchRecipesUser(List<String> keywords, List<String> ingredientIds, Boolean vegetarian,
                                                Integer covers, Boolean lunchBox, List<String> allergyIds, UUID creatorId) {

        // Vérifications initiales pour éviter des erreurs
        if (creatorId == null) {
            throw new IllegalArgumentException("creatorId ne peut pas être null");
        }

        List<String> allKeywords = keywords.stream()
                .flatMap(keyword -> Arrays.stream(keyword.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        List<String> allIngredientIds = ingredientIds.stream()
                .flatMap(id -> Arrays.stream(id.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        List<String> allAllergyIds = allergyIds.stream()
                .flatMap(id -> Arrays.stream(id.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();


        StringBuilder query = new StringBuilder("SELECT r FROM RecipeEntity r WHERE r.recipePublic = false AND r.user.id = :creatorId");

        Map<String, Object> params = new HashMap<>();
        params.put("creatorId", creatorId);

        // Gestion des Keywords
        if (!allKeywords.isEmpty()) {
            query.append(" AND (");
            for (int i = 0; i < allKeywords.size(); i++) {
                query.append("(LOWER(r.recipeName) LIKE :keyword").append(i)
                        .append(" OR LOWER(r.recipeInstructions) LIKE :keyword").append(i).append(")");
                if (i < allKeywords.size() - 1) query.append(" OR ");
                params.put("keyword" + i, "%" + allKeywords.get(i).toLowerCase() + "%");
            }
            query.append(")");
        }

        // Gestion des ingrédients
        if (!allIngredientIds.isEmpty()) {
            query.append(" AND EXISTS (");
            query.append("    SELECT 1 FROM RecipesIngredientsEntity ri ");
            query.append("    WHERE ri.recipe.idRecipe = r.idRecipe ");
            query.append("    AND ri.ingredient.idIngredient IN :ingredientIds ");
            query.append("    GROUP BY ri.recipe.idRecipe ");
            query.append("    HAVING COUNT(DISTINCT ri.ingredient.idIngredient) = :ingredientCount");
            query.append(")");
            params.put("ingredientIds", allIngredientIds);
            params.put("ingredientCount", allIngredientIds.size());
        }

        // Gestion du filtre végétarien
        if (vegetarian != null) {
            query.append(vegetarian ? " AND NOT EXISTS (" : " AND EXISTS (");
            query.append("   SELECT 1 FROM RecipesIngredientsEntity ri ");
            query.append("   WHERE ri.recipe.idRecipe = r.idRecipe ");
            query.append("   AND ri.ingredient.ingredientIsVegan = false ");
            query.append(" )");
        }

        // Gestion du nombre de couverts
        if (covers != null) {
            query.append(" AND r.recipeNbCovers = :covers");
            params.put("covers", covers);
        }

        // Gestion du lunch box
        if (lunchBox != null) {
            query.append(" AND r.recipeLunchBox = :lunchBox");
            params.put("lunchBox", lunchBox);
        }

        // Gestion des allergies
        List<Long> validAllergyIds = allAllergyIds.stream()
                .filter(id -> id.matches("\\d+")) // Vérifie si c'est un nombre
                .map(Long::valueOf)
                .toList();

        List<Long> existingAllergyIds = findExistingAllergyIds(validAllergyIds);

        if (!existingAllergyIds.isEmpty()) {
            query.append(" AND NOT EXISTS (");
            query.append("   SELECT 1 FROM RecipesIngredientsEntity ri ");
            query.append("   JOIN ri.ingredient i ");
            query.append("   JOIN i.allergies a ");
            query.append("   WHERE ri.recipe.idRecipe = r.idRecipe ");
            query.append("   AND a.id IN :allergyIds");
            query.append(" )");
            params.put("allergyIds", existingAllergyIds);
        }

        // Création et exécution de la requête
        TypedQuery<RecipeEntity> typedQuery = getEntityManager().createQuery(query.toString(), RecipeEntity.class);
        params.forEach(typedQuery::setParameter);

        return typedQuery.getResultList();
    }







    /**
     * recherche de recette publique avec des filtre sur
     * @param keywords
     * @param ingredientIds
     * @param vegetarian
     * @param covers
     * @param lunchBox
     * @param allergyIds
     * @return
     */
    public List<RecipeEntity> searchRecipes(List<String> keywords, List<String> ingredientIds, Boolean vegetarian,
                                            Integer covers, Boolean lunchBox,List<String> allergyIds ) {


        List<String> allKeywords = new ArrayList<>();
        List<String> allIngredientIds = new ArrayList<>();
        List<String> allAllergyIds = new ArrayList<>();



        for (String keyword : keywords) {
            String[] splitKeywords = keyword.split(",");
            allKeywords.addAll(Arrays.asList(splitKeywords));
        }

        for (String ingredientId : ingredientIds) {
            String[] splitIngredientId = ingredientId.split(",");
            allIngredientIds.addAll(Arrays.asList(splitIngredientId));
        }

        for(String allergie : allergyIds) {
            String[] splitAllergie = allergie.split(",");
            allAllergyIds.addAll(Arrays.asList(splitAllergie));
        }



        //recupérer toutes les recettes dans l alias r
        StringBuilder query = new StringBuilder("SELECT r FROM RecipeEntity r WHERE r.recipePublic=true and 1=1");
        Map<String, Object> params = new HashMap<>();


        if (!allKeywords.isEmpty()) {
            query.append(" AND (");

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

        // Convertir les allergyIds en Long en filtrant les valeurs invalides
        List<Long> validAllergyIds = allAllergyIds.stream()
                .filter(id -> id != null && !id.trim().isEmpty())  // Supprime les null et chaînes vides
                .filter(id -> id.matches("\\d+")) // si c est un nombre
                .map(Long::valueOf) // Convertit en Long
                .collect(Collectors.toList());

        // Vérifier si les allergies existent avant d'ajouter le filtre
        List<Long> existingAllergyIds = findExistingAllergyIds(validAllergyIds);
//        System.out.println(STR."here->\{existingAllergyIds}");

        if (!allAllergyIds.isEmpty() && !existingAllergyIds.isEmpty()) {
            query.append(" AND NOT EXISTS (");
            query.append("   SELECT 1 FROM RecipesIngredientsEntity ri ");
            query.append("   JOIN ri.ingredient i ");
            query.append("   JOIN i.allergies a ");
            query.append("   WHERE ri.recipe.idRecipe = r.idRecipe ");
            query.append("   AND a.id IN :allergyIds");
            query.append(" )");

            params.put("allergyIds", existingAllergyIds);
        }

        TypedQuery<RecipeEntity> typedQuery = getEntityManager().createQuery(query.toString(), RecipeEntity.class);
        params.forEach(typedQuery::setParameter);

        return typedQuery.getResultList();
    }


    public List<Long> findExistingAllergyIds(List<Long> allergyIds) {
        if (allergyIds == null || allergyIds.isEmpty()) {
            return Collections.emptyList();
        }

        return getEntityManager().createQuery(
                        "SELECT a.id FROM AllergyEntity a WHERE a.id IN :ids", Long.class)
                .setParameter("ids", allergyIds)
                .getResultList();
    }


}
