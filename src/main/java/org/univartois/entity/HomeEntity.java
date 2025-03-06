package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;
import org.univartois.utils.Constants;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Table(name = "homes")
@Entity
@NamedQueries({
        @NamedQuery(
                name = Constants.QUERY_FIND_HOMES_BY_USER_ID,
                query = "SELECT home FROM HomeRoleEntity homeRole " +
                        "RIGHT JOIN homeRole.home home " +
                        "WHERE homeRole.id.userId = :userId"
        )
})
public class HomeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private boolean vegetarian;

    private boolean lunchAutomaticGeneration;

    private boolean dinerAutomaticGeneration;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "homes_allergies", joinColumns = @JoinColumn(name = "home_id"), inverseJoinColumns = @JoinColumn(name = "allergy_id"))
    private Set<AllergyEntity> allergies = new HashSet<>();


}
