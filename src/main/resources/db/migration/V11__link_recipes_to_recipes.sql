-- Création de l'utilisateur
DO $$
DECLARE
user_id UUID;
BEGIN
    user_id := gen_random_uuid();

INSERT INTO users (
    id, username, email, firstname, lastname, password, profilePictureUrl, vegetarian, verified
) VALUES (
             user_id, 'chef_gourmet', 'chef.gourmet@example.com', 'Chef', 'Gourmet', 'hashed_password', 'https://example.com/profile.jpg', FALSE, TRUE
         );

-- Lier l'utilisateur aux recettes
UPDATE recipes
SET id_creator = user_id
WHERE recipeName IN ('Blanquette de Veau', 'Ratatouille', 'Coq au Vin', 'Tarte Tatin');
END $$;