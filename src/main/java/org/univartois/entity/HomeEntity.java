package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;
import org.univartois.utils.Constants;

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
                query = "SELECT home FROM HomeEntity home " +
                        "JOIN HomeRoleEntity homeRole ON home.id = homeRole.id.homeId " +
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


}
