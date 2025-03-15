package org.univartois.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

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
 //   @NotNull(message = "L'image de la recette est obligatoire.")
    private String recipeImageLink;

    @Size(min = 2, max = 30, message = "Le nom de la recette doit contenir entre 2 et 30 caractères.")
    private String recipeName;

    @Column(name = "prep_time_minutes")
    private int prepTimeMinutes;

   // @Pattern(regexp = "^https://youtu\\.be/[_a-zA-Z0-9]{11}(\\?.*)?$", message = "Lien YouTube invalide")
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

    //recuperer le nom complet du user
    public String getFullName() {
        return user != null ? user.getFirstname() + " " + user.getLastname() : null;
    }


    @Transient
    List<IngredientEntity> ingredientsList = new ArrayList<>();


}