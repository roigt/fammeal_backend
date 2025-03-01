package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRecipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creator", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "recipe_image_link")
    private String recipeImageLink;

    private String recipeName;

    @Column(name = "prep_time_minutes")
    private int prepTimeMinutes;

    @Column(name = "recipe_video_link")
    private String recipeVideoLink;

    @Column(name = "recipe_instructions", columnDefinition = "TEXT")
    private String recipeInstructions;

    @Column(name = "recipe_public")
    private boolean recipePublic;

    @Column(name = "recipe_lunch_box")
    private boolean recipeLunchBox;

    @Column(name = "recipe_nb_covers")
    private int recipeNbCovers;

    @Column(name = "cook_time_minutes")
    private int cookTimeMinutes;

    @OneToMany(mappedBy = "recipe")
    public List<RecipesIngredientsEntity> recipesIngredients = new ArrayList<>();


}