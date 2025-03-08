package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "proposed_meals")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProposedMealEntity.ProposedMealId.class)
public class ProposedMealEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRecipe", referencedColumnName = "idRecipe", nullable = true)
    private RecipeEntity recipe;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_meal", referencedColumnName = "id_meal", nullable = false)
    private MealEntity meal;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private UserEntity proposer;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProposedMealId implements Serializable {
        private UUID recipe;
        private UUID meal;
        private UUID proposer;
    }
}