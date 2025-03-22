
-- Link ingredients to recipes
INSERT INTO recipes_ingredients (quantity_need, id_recipe, id_ingredient)
VALUES
    -- Blanquette de Veau
    (200, (SELECT idRecipe FROM recipes WHERE recipeName = 'Blanquette de Veau'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lait entier')),
    (100, (SELECT idRecipe FROM recipes WHERE recipeName = 'Blanquette de Veau'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Beurre')),

    -- Ratatouille
    (150, (SELECT idRecipe FROM recipes WHERE recipeName = 'Ratatouille'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lait écrémé')),
    (200, (SELECT idRecipe FROM recipes WHERE recipeName = 'Ratatouille'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Emmental')),

    -- Coq au Vin
    (250, (SELECT idRecipe FROM recipes WHERE recipeName = 'Coq au Vin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Camembert')),
    (100, (SELECT idRecipe FROM recipes WHERE recipeName = 'Coq au Vin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crème fraîche')),

    -- Tarte Tatin
    (200, (SELECT idRecipe FROM recipes WHERE recipeName = 'Tarte Tatin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Yaourt nature')),
    (150, (SELECT idRecipe FROM recipes WHERE recipeName = 'Tarte Tatin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Beurre'));
