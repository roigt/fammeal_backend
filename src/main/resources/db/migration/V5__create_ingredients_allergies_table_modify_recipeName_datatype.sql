CREATE TABLE ingredients_allergies
(
    allergy_id    BIGINT NOT NULL,
    ingredient_id UUID   NOT NULL,
    CONSTRAINT pk_ingredients_allergies PRIMARY KEY (allergy_id, ingredient_id)
);

ALTER TABLE ingredients_allergies
    ADD CONSTRAINT fk_ingall_on_allergy_entity FOREIGN KEY (allergy_id) REFERENCES allergies (id);

ALTER TABLE ingredients_allergies
    ADD CONSTRAINT fk_ingall_on_ingredient_entity FOREIGN KEY (ingredient_id) REFERENCES ingredients (idIngredient);

ALTER TABLE recipes
ALTER COLUMN recipeName TYPE VARCHAR(30) USING (recipeName::VARCHAR(30));