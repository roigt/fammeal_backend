package org.univartois.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.univartois.enums.IngredientUnit;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idIngredient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientUnit idUnit;

    @Column(nullable = false, unique = true)
    private String ingredientName;

    @Column(nullable = false)
    private boolean ingredientIsVegan;

    @Column(nullable = false)
    private int nbDayBeforeExpiration;

    @JsonIgnore
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    public List<RecipesIngredientsEntity> recipesIngredients;
}
