CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Insert ingredients
INSERT INTO ingredients (idIngredient, idUnit, ingredientName, ingredientIsVegan, nbDayBeforeExpiration)
VALUES
    -- Dairy products
    (uuid_generate_v4(), 'LITER', 'Lait entier', FALSE, 7),
    (uuid_generate_v4(), 'LITER', 'Lait écrémé', FALSE, 7),
    (uuid_generate_v4(), 'GRAM', 'Fromage blanc', FALSE, 10),
    (uuid_generate_v4(), 'GRAM', 'Emmental', FALSE, 30),
    (uuid_generate_v4(), 'GRAM', 'Camembert', FALSE, 14),
    (uuid_generate_v4(), 'GRAM', 'Yaourt nature', FALSE, 14),
    (uuid_generate_v4(), 'GRAM', 'Beurre', FALSE, 30),
    (uuid_generate_v4(), 'LITER', 'Crème fraîche', FALSE, 14),
    (uuid_generate_v4(), 'LITER', 'Lait de chèvre', FALSE, 7),
    (uuid_generate_v4(), 'LITER', 'Lait de brebis', FALSE, 7),

    -- Eggs
    (uuid_generate_v4(), 'UNIT', 'Œufs', FALSE, 28),

    -- Nuts and seeds
    (uuid_generate_v4(), 'GRAM', 'Arachides', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Noix', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Amandes', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Noisettes', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Pistaches', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Graines de sésame', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Graines de tournesol', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Châtaignes', TRUE, 30),

    -- Cereals and grains
    (uuid_generate_v4(), 'GRAM', 'Farine de blé', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Farine de seigle', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Farine d''orge', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Farine d''avoine', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Farine d''épeautre', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Riz', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Maïs', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Quinoa', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Sarrasin', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Millet', TRUE, 365),

    -- Legumes
    (uuid_generate_v4(), 'GRAM', 'Soja', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Tofu', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Haricots rouges', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Lentilles', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Pois chiches', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Petits pois', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Haricots verts', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Lupin', TRUE, 365),

    -- Seafood
    (uuid_generate_v4(), 'GRAM', 'Saumon', FALSE, 3),
    (uuid_generate_v4(), 'GRAM', 'Thon', FALSE, 3),
    (uuid_generate_v4(), 'GRAM', 'Cabillaud', FALSE, 3),
    (uuid_generate_v4(), 'GRAM', 'Crevettes', FALSE, 2),
    (uuid_generate_v4(), 'GRAM', 'Moules', FALSE, 2),
    (uuid_generate_v4(), 'GRAM', 'Huîtres', FALSE, 2),
    (uuid_generate_v4(), 'GRAM', 'Langouste', FALSE, 2),
    (uuid_generate_v4(), 'GRAM', 'Crabe', FALSE, 2),
    (uuid_generate_v4(), 'GRAM', 'Calamar', FALSE, 3),
    (uuid_generate_v4(), 'GRAM', 'Escargots', FALSE, 5),

    -- Meat
    (uuid_generate_v4(), 'GRAM', 'Bœuf', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Poulet', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Porc', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Agneau', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Dinde', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Canard', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Lapin', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Cheval', FALSE, 5),
    (uuid_generate_v4(), 'GRAM', 'Foie gras', FALSE, 14),

    -- Fruits
    (uuid_generate_v4(), 'GRAM', 'Pomme', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Poire', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Banane', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Fraise', TRUE, 5),
    (uuid_generate_v4(), 'GRAM', 'Framboise', TRUE, 5),
    (uuid_generate_v4(), 'GRAM', 'Kiwi', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Ananas', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Pêche', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Abricot', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Melon', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Pastèque', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Figue', TRUE, 5),
    (uuid_generate_v4(), 'GRAM', 'Raisin', TRUE, 7),

    -- Vegetables
    (uuid_generate_v4(), 'GRAM', 'Tomate', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Concombre', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Carotte', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Courgette', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Aubergine', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Poivron', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Oignon', TRUE, 30),
    (uuid_generate_v4(), 'GRAM', 'Ail', TRUE, 30),
    (uuid_generate_v4(), 'GRAM', 'Pomme de terre', TRUE, 60),
    (uuid_generate_v4(), 'GRAM', 'Chou', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Champignon', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Céleri', TRUE, 14),
    (uuid_generate_v4(), 'GRAM', 'Radis', TRUE, 10),

    -- Herbs and spices
    (uuid_generate_v4(), 'GRAM', 'Persil', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Basilic', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Coriandre', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Menthe', TRUE, 7),
    (uuid_generate_v4(), 'GRAM', 'Thym', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Cannelle', TRUE, 730),
    (uuid_generate_v4(), 'GRAM', 'Curcuma', TRUE, 730),
    (uuid_generate_v4(), 'GRAM', 'Poivre', TRUE, 730),
    (uuid_generate_v4(), 'GRAM', 'Moutarde', TRUE, 365),

    -- Other
    (uuid_generate_v4(), 'GRAM', 'Chocolat noir', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Chocolat au lait', FALSE, 180),
    (uuid_generate_v4(), 'GRAM', 'Miel', TRUE, 730),
    (uuid_generate_v4(), 'LITER', 'Sauce soja', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Algues nori', TRUE, 180),
    (uuid_generate_v4(), 'GRAM', 'Gélatine', FALSE, 365),
    (uuid_generate_v4(), 'GRAM', 'Levure chimique', TRUE, 365),
    (uuid_generate_v4(), 'GRAM', 'Colorant alimentaire', TRUE, 730),
    (uuid_generate_v4(), 'GRAM', 'Conservateur', TRUE, 730);

-- Function to get allergy ID
CREATE OR REPLACE FUNCTION get_allergy_id(allergy_name VARCHAR) RETURNS BIGINT AS $$
DECLARE
    allergy_id BIGINT;
BEGIN
    SELECT id INTO allergy_id FROM allergies WHERE name = allergy_name;
    RETURN allergy_id;
END;
$$ LANGUAGE plpgsql;

-- Insert the links between ingredients and allergies
INSERT INTO ingredients_allergies (allergy_id, ingredient_id)
VALUES
    -- Dairy products with milk allergy
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lait entier')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lait écrémé')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Fromage blanc')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Emmental')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Camembert')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Yaourt nature')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Beurre')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crème fraîche')),
    (get_allergy_id('Lait de chèvre'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lait de chèvre')),
    (get_allergy_id('Lait de brebis'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lait de brebis')),
    (get_allergy_id('Fromage'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Emmental')),
    (get_allergy_id('Fromage'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Camembert')),
    (get_allergy_id('Yaourt'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Yaourt nature')),
    (get_allergy_id('Crème fraîche'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crème fraîche')),
    (get_allergy_id('Beurre'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Beurre')),

    -- Eggs
    (get_allergy_id('Œufs'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Œufs')),

    -- Nuts and seeds
    (get_allergy_id('Arachides'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Arachides')),
    (get_allergy_id('Cacahuète'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Arachides')),
    (get_allergy_id('Fruits à coque'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Noix')),
    (get_allergy_id('Fruits à coque'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Amandes')),
    (get_allergy_id('Fruits à coque'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Noisettes')),
    (get_allergy_id('Fruits à coque'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Pistaches')),
    (get_allergy_id('Noix'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Noix')),
    (get_allergy_id('Amande'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Amandes')),
    (get_allergy_id('Noisette'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Noisettes')),
    (get_allergy_id('Pistache'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Pistaches')),
    (get_allergy_id('Sésame'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Graines de sésame')),
    (get_allergy_id('Châtaigne'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Châtaignes')),

    -- Cereals and grains
    (get_allergy_id('Blé'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine de blé')),
    (get_allergy_id('Farine de blé'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine de blé')),
    (get_allergy_id('Gluten'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine de blé')),
    (get_allergy_id('Gluten'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine de seigle')),
    (get_allergy_id('Gluten'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine d''orge')),
    (get_allergy_id('Gluten'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine d''avoine')),
    (get_allergy_id('Gluten'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine d''épeautre')),
    (get_allergy_id('Seigle'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine de seigle')),
    (get_allergy_id('Orge'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine d''orge')),
    (get_allergy_id('Avoine'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine d''avoine')),
    (get_allergy_id('Épeautre'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Farine d''épeautre')),
    (get_allergy_id('Riz'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Riz')),
    (get_allergy_id('Maïs'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Maïs')),
    (get_allergy_id('Quinoa'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Quinoa')),
    (get_allergy_id('Sarrasin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Sarrasin')),
    (get_allergy_id('Millet'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Millet')),

    -- Legumes
    (get_allergy_id('Soja'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Soja')),
    (get_allergy_id('Soja'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Tofu')),
    (get_allergy_id('Tofu'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Tofu')),
    (get_allergy_id('Petit pois'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Petits pois')),
    (get_allergy_id('Haricot vert'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Haricots verts')),
    (get_allergy_id('Lupin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lupin')),

    -- Seafood
    (get_allergy_id('Poisson'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Saumon')),
    (get_allergy_id('Poisson'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Thon')),
    (get_allergy_id('Poisson'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Cabillaud')),
    (get_allergy_id('Saumon'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Saumon')),
    (get_allergy_id('Thon'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Thon')),
    (get_allergy_id('Crustacés'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crevettes')),
    (get_allergy_id('Crustacés'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Langouste')),
    (get_allergy_id('Crustacés'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crabe')),
    (get_allergy_id('Mollusques'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Moules')),
    (get_allergy_id('Mollusques'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Huîtres')),
    (get_allergy_id('Mollusques'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Calamar')),
    (get_allergy_id('Mollusques'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Escargots')),
    (get_allergy_id('Huîtres'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Huîtres')),
    (get_allergy_id('Calamar'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Calamar')),
    (get_allergy_id('Crevettes'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crevettes')),
    (get_allergy_id('Langouste'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Langouste')),
    (get_allergy_id('Crabe'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Crabe')),
    (get_allergy_id('Escargots'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Escargots')),

    -- Meat
    (get_allergy_id('Viande de boeuf'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Bœuf')),
    (get_allergy_id('Viande de poulet'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Poulet')),
    (get_allergy_id('Viande de porc'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Porc')),
    (get_allergy_id('Dinde'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Dinde')),
    (get_allergy_id('Canard'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Canard')),
    (get_allergy_id('Lapin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Lapin')),
    (get_allergy_id('Viande de cheval'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Cheval')),
    (get_allergy_id('Foie gras'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Foie gras')),

    -- Fruits
    (get_allergy_id('Pomme'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Pomme')),
    (get_allergy_id('Poire'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Poire')),
    (get_allergy_id('Banane'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Banane')),
    (get_allergy_id('Fraise'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Fraise')),
    (get_allergy_id('Framboise'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Framboise')),
    (get_allergy_id('Kiwi'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Kiwi')),
    (get_allergy_id('Ananas'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Ananas')),
    (get_allergy_id('Pêche'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Pêche')),
    (get_allergy_id('Abricot'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Abricot')),
    (get_allergy_id('Figue'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Figue')),
    (get_allergy_id('Raisin'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Raisin')),

    -- Vegetables
    (get_allergy_id('Tomate'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Tomate')),
    (get_allergy_id('Carotte'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Carotte')),
    (get_allergy_id('Céleri'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Céleri')),
    (get_allergy_id('Pomme de terre'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Pomme de terre')),
    (get_allergy_id('Oignon'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Oignon')),
    (get_allergy_id('Ail'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Ail')),
    (get_allergy_id('Champignon'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Champignon')),
    (get_allergy_id('Radis'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Radis')),
    (get_allergy_id('Chou'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Chou')),
    (get_allergy_id('Poivron'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Poivron')),
    (get_allergy_id('Aubergine'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Aubergine')),
    (get_allergy_id('Courgette'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Courgette')),
    (get_allergy_id('Concombre'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Concombre')),

    -- Herbs and spices
    (get_allergy_id('Persil'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Persil')),
    (get_allergy_id('Basilic'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Basilic')),
    (get_allergy_id('Coriandre'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Coriandre')),
    (get_allergy_id('Menthe'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Menthe')),
    (get_allergy_id('Cannelle'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Cannelle')),
    (get_allergy_id('Curcuma'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Curcuma')),
    (get_allergy_id('Moutarde'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Moutarde')),

    -- Other
    (get_allergy_id('Chocolat'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Chocolat noir')),
    (get_allergy_id('Chocolat'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Chocolat au lait')),
    (get_allergy_id('Lait'), (SELECT idIngredient FROM ingredients WHERE ingredientName = 'Chocolat au lait'));