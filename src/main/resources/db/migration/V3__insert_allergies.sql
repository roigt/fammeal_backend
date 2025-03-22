CREATE TABLE ingredients
(
    idIngredient          UUID         NOT NULL,
    idUnit                VARCHAR(255) NOT NULL,
    ingredientName        VARCHAR(255) NOT NULL,
    ingredientIsVegan     BOOLEAN      NOT NULL,
    nbDayBeforeExpiration INTEGER      NOT NULL,
    CONSTRAINT pk_ingredients PRIMARY KEY (idIngredient)
);

CREATE TABLE meals
(
    id_meal    UUID    NOT NULL,
    meal_date  date    NOT NULL,
    meal_lunch BOOLEAN NOT NULL,
    id_home    UUID,
    idRecipe   UUID,
    CONSTRAINT pk_meals PRIMARY KEY (id_meal)
);

CREATE TABLE pantry_ingredients
(
    idIngredientInPantry     UUID  NOT NULL,
    idIngredient             UUID  NOT NULL,
    id_home                  UUID  NOT NULL,
    ingredientQuantity       FLOAT NOT NULL,
    ingredientExpirationDate date,
    id_user                  UUID,
    CONSTRAINT pk_pantry_ingredients PRIMARY KEY (idIngredientInPantry)
);

CREATE TABLE proposed_meals
(
    idRecipe UUID NOT NULL,
    id_meal  UUID NOT NULL,
    id       UUID NOT NULL,
    CONSTRAINT pk_proposed_meals PRIMARY KEY (idRecipe, id_meal, id)
);

CREATE TABLE recipes
(
    idRecipe            UUID NOT NULL,
    id_creator          UUID,
    recipe_image_link   VARCHAR(255),
    recipeName          VARCHAR(255),
    prep_time_minutes   INTEGER,
    recipe_video_link   VARCHAR(255),
    recipe_instructions TEXT,
    recipe_public       BOOLEAN,
    recipe_lunch_box    BOOLEAN,
    recipe_nb_covers    INTEGER,
    cook_time_minutes   INTEGER,
    CONSTRAINT pk_recipes PRIMARY KEY (idRecipe)
);

CREATE TABLE recipes_ingredients
(
    quantity_need FLOAT NOT NULL,
    id_recipe     UUID  NOT NULL,
    id_ingredient UUID  NOT NULL,
    CONSTRAINT pk_recipes_ingredients PRIMARY KEY (id_recipe, id_ingredient)
);

ALTER TABLE ingredients
    ADD CONSTRAINT uc_ingredients_ingredientname UNIQUE (ingredientName);

ALTER TABLE meals
    ADD CONSTRAINT FK_MEALS_ON_IDRECIPE FOREIGN KEY (idRecipe) REFERENCES recipes (idRecipe);

ALTER TABLE meals
    ADD CONSTRAINT FK_MEALS_ON_ID_HOME FOREIGN KEY (id_home) REFERENCES homes (id);

ALTER TABLE pantry_ingredients
    ADD CONSTRAINT FK_PANTRY_INGREDIENTS_ON_IDINGREDIENT FOREIGN KEY (idIngredient) REFERENCES ingredients (idIngredient);

ALTER TABLE pantry_ingredients
    ADD CONSTRAINT FK_PANTRY_INGREDIENTS_ON_ID_HOME FOREIGN KEY (id_home) REFERENCES homes (id);

ALTER TABLE pantry_ingredients
    ADD CONSTRAINT FK_PANTRY_INGREDIENTS_ON_ID_USER FOREIGN KEY (id_user) REFERENCES users (id);

ALTER TABLE proposed_meals
    ADD CONSTRAINT FK_PROPOSED_MEALS_ON_ID FOREIGN KEY (id) REFERENCES users (id);

ALTER TABLE proposed_meals
    ADD CONSTRAINT FK_PROPOSED_MEALS_ON_IDRECIPE FOREIGN KEY (idRecipe) REFERENCES recipes (idRecipe);

ALTER TABLE proposed_meals
    ADD CONSTRAINT FK_PROPOSED_MEALS_ON_ID_MEAL FOREIGN KEY (id_meal) REFERENCES meals (id_meal);

ALTER TABLE recipes_ingredients
    ADD CONSTRAINT FK_RECIPES_INGREDIENTS_ON_ID_INGREDIENT FOREIGN KEY (id_ingredient) REFERENCES ingredients (idIngredient);

ALTER TABLE recipes_ingredients
    ADD CONSTRAINT FK_RECIPES_INGREDIENTS_ON_ID_RECIPE FOREIGN KEY (id_recipe) REFERENCES recipes (idRecipe);

ALTER TABLE recipes
    ADD CONSTRAINT FK_RECIPES_ON_ID_CREATOR FOREIGN KEY (id_creator) REFERENCES users (id);