package org.univartois.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.*;
import org.univartois.enums.IngredientUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER)
    public List<RecipesIngredientsEntity> recipesIngredients;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ingredients_allergies", joinColumns = @JoinColumn(name = "ingredient_id"), inverseJoinColumns = @JoinColumn(name = "allergy_id"))
    private Set<AllergyEntity> allergies = new HashSet<>();


//    public String getAllAllergies(){
//        return allergies !=null ? Arrays.toString(allergies.stream().map(allergyEntity -> "id: "+ allergyEntity.getId() +", name: "+allergyEntity.getName()).toArray()):null;
//     }

    public Map<Long, String> getAllAllergies() {
        return allergies != null
                ? allergies.stream()
                .collect(Collectors.toMap(AllergyEntity::getId, AllergyEntity::getName))
                : Map.of();
    }



}
