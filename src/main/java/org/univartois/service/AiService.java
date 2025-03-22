package org.univartois.service;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import org.univartois.enums.IngredientUnit;

import java.util.List;
import java.util.Set;

@RegisterAiService
public interface AiService {

    @UserMessage("""
            Vous êtes un Chef d'un restaurant 5 étoiles, spécialisé dans les recettes respectant des contraintes alimentaires strictes.
            **Répondez uniquement en français.**
            
            Votre tâche : générer une recette créative en tenant compte des allergies et préférences alimentaires données.
            
            Paramètres :
                - "allergies": {allergies}
                - "vegetarian": {vegetarian}
                - **Voici une liste des noms des ingrédients : {ingredients} .**
            
                ####
                # Contraintes :
                Retournez uniquement un JSON valide avec les règles suivantes :
            
                    - **`recipeName` doit contenir entre 2 et 30 caractères.**
                    - **Le nom des ingrédients doit être en singulier.**
                    - **Aucun ingrédient ne doit contenir les allergies suivantes : {allergies}.**
                    - **le champs `unit` pour chaque ingrédient doit être l'une de ces valeurs: [GRAM, KILOGRAM, LITER, MILLILITER, UNIT] .**
                    - **Si `vegetarian` est vrai, tous les ingrédients doivent être végétariens.**
                    - **La durée de conservation (`nbDayBeforeExpiration`) doit être réaliste en fonction de l’ingrédient (par exemple : la viande a une conservation courte, mais le sel peut durer plusieurs années).**
                    - **L’unité doit correspondre logiquement à l’ingrédient (par exemple : "UNIT" pour un œuf, mais pas pour de l’huile).**
                    - **Expérimentez avec des styles de cuisine variés (par exemple asiatique, méditerranéen, fusion, etc.).**
                    ###
            
                    Vous devez répondre strictement dans le format JSON suivant :
                    {
                        "recipeImageLink": "(type: string)",
                        "recipeName": "(type: string)",
                        "prepTimeMinutes": "(type: integer)",
                        "recipeInstructions": "(type: string)",
                        "recipeLunchBox": "(type: boolean)",
                        "recipeNbCovers": "(type: integer)",
                        "cookTimeMinutes": "(type: integer)",
                        "ingredients": [
                                            {
                                                "ingredientName": "(type: string)",
                                                "quantityNeed": "(type: float)",
                                                "vegan": "(type: boolean)",
                                                "unit": "(type: enum, must be one of [GRAM, KILOGRAM, LITER, MILLILITER, UNIT])",
                                                "nbDayBeforeExpiration": "(type: integer)"
                                            }
                                        ]
                    }
            """)
    RecipeAiDto generateRecipe(Set<String> allergies, Set<String> ingredients, boolean vegetarian);

    record RecipeAiDto(
            String recipeImageLink,
            String recipeName,
            int prepTimeMinutes,
            String recipeInstructions,
            boolean recipeLunchBox,
            int recipeNbCovers,
            int cookTimeMinutes,
            List<IngredientAiDto> ingredients
    ) {

        @Override
        public String recipeImageLink() {
            return "https://res.cloudinary.com/dhgjfj1ac/image/upload/v1742622394/evanksbmljgcy7elvswq.avif";
        }
    }

    record IngredientAiDto(
            String ingredientName,
            float quantityNeed,
            boolean vegan,
            IngredientUnit unit,
            int nbDayBeforeExpiration
    ) {

    }
}
