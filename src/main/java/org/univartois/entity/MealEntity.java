package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "meals")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_meal")
    private UUID idMeal;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Column(name = "meal_lunch", nullable = false)
    private boolean mealLunch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_home", referencedColumnName = "id", nullable = true)
    private HomeEntity home;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRecipe", referencedColumnName = "idRecipe", nullable = true)
    private RecipeEntity recipe;
}