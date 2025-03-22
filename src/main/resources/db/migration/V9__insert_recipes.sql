INSERT INTO recipes (
    idRecipe, id_creator, recipe_image_link, recipeName,
    prep_time_minutes, recipe_video_link, recipe_instructions,
    recipe_public, recipe_lunch_box, recipe_nb_covers, cook_time_minutes
) VALUES
      (
          gen_random_uuid(), NULL,
          'https://asset.cloudinary.com/dz5xkou3q/77f57a15f6eafc4292d50db5e038da1a',
          'Blanquette de Veau',
          30, 'https://www.youtube.com/watch?v=xn1BTnRmEb8',
          '1. Couper la viande et la faire revenir...\n2. Ajouter les légumes...\n3. Laisser mijoter 1h30...',
          TRUE, FALSE, 4, 90
      ),
      (
          gen_random_uuid(), NULL,
          'https://asset.cloudinary.com/dz5xkou3q/e0d34ebee1bd06e41f8862350e7de2b4',
          'Ratatouille',
          20, 'https://www.youtube.com/watch?v=1cHP_-AMquQ',
          '1. Couper les légumes en rondelles...\n2. Faire revenir dans l''huile d''olive...\n3. Cuire au four à 180°C pendant 45 min...',
          TRUE, TRUE, 4, 45
      ),
      (
          gen_random_uuid(), NULL,
          'https://asset.cloudinary.com/dz5xkou3q/daced94a3f6e7c50867fa4a0d484bc30',
          'Coq au Vin',
          40, 'https://www.youtube.com/watch?v=V1mWbdejSuk',
          '1. Faire mariner le coq dans du vin rouge...\n2. Faire revenir les morceaux avec des lardons et des oignons...\n3. Laisser mijoter 2h...',
          TRUE, FALSE, 6, 120
      ),
      (
          gen_random_uuid(), NULL,
          'https://asset.cloudinary.com/dz5xkou3q/9911e70410c7b8ff7e7bc1bd27b50488',
          'Tarte Tatin',
          15, 'https://www.youtube.com/watch?v=JQb9nFIcufs',
          '1. Caraméliser le sucre et le beurre dans un moule...\n2. Ajouter les pommes coupées en quartiers...\n3. Recouvrir de pâte et cuire au four à 180°C pendant 35 min...',
          TRUE, FALSE, 6, 35
      );


