package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;
import org.univartois.dto.response.IngredientResponseDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "pantry_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PantryIngredientEntity {

    @Transient
    public IngredientEntity ingredientDetails;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idIngredientInPantry;

    @ManyToOne
    @JoinColumn(name = "idIngredient", nullable = false)
    private IngredientEntity ingredient;

    @ManyToOne
    @JoinColumn(name = "id_home", nullable = false)
    private HomeEntity home;


    @Column(nullable = false)
    private float ingredientQuantity;


    private LocalDate ingredientExpirationDate;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = true)
    private UserEntity user;

}
