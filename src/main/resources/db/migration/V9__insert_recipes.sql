INSERT INTO recipes (
    idRecipe, id_creator, recipe_image_link, recipeName,
    prep_time_minutes, recipe_video_link, recipe_instructions,
    recipe_public, recipe_lunch_box, recipe_nb_covers, cook_time_minutes
) VALUES
      (
          gen_random_uuid(), NULL,
          'https://res.cloudinary.com/dz5xkou3q/image/upload/v1742697552/42c72c64-1b54-4642-8b97-90a26f1310a1.jpg',
          'Blanquette de Veau',
          30, 'https://www.youtube.com/watch?v=xn1BTnRmEb8',
          '1. Couper la viande et la faire revenir...\n2. Ajouter les légumes...\n3. Laisser mijoter 1h30...',
          TRUE, FALSE, 4, 90
      ),
      (
          gen_random_uuid(), NULL,
          'https://res.cloudinary.com/dz5xkou3q/image/upload/v1742697620/0669373d-7ed3-448a-bbcd-8ccf7fca744a.jpg',
          'Ratatouille',
          20, 'https://www.youtube.com/watch?v=1cHP_-AMquQ',
          '1. Couper les légumes en rondelles...\n2. Faire revenir dans l''huile d''olive...\n3. Cuire au four à 180°C pendant 45 min...',
          TRUE, TRUE, 4, 45
      ),
      (
          gen_random_uuid(), NULL,
          'https://res.cloudinary.com/dz5xkou3q/image/upload/v1742697662/e8acf353-58a5-4c72-ab55-9dd19fda5f8b.jpg',
          'Coq au Vin',
          40, 'https://www.youtube.com/watch?v=V1mWbdejSuk',
          '1. Faire mariner le coq dans du vin rouge...\n2. Faire revenir les morceaux avec des lardons et des oignons...\n3. Laisser mijoter 2h...',
          TRUE, FALSE, 6, 120
      ),
      (
          gen_random_uuid(), NULL,
          'https://res.cloudinary.com/dz5xkou3q/image/upload/v1742697710/020ca566-ff97-46c1-a6a8-89c86b7d6e8b.jpg',
          'Tarte Tatin',
          15, 'https://www.youtube.com/watch?v=JQb9nFIcufs',
          '1. Caraméliser le sucre et le beurre dans un moule...\n2. Ajouter les pommes coupées en quartiers...\n3. Recouvrir de pâte et cuire au four à 180°C pendant 35 min...',
          TRUE, FALSE, 6, 35
      );


