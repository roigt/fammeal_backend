package org.univartois.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.univartois.utils.Constants;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@NamedQueries({
        @NamedQuery(
                name = Constants.QUERY_FIND_USERS_BY_HOME_ID,
                query = "SELECT user FROM UserEntity user " +
                        "JOIN FETCH user.roles homeRole " +
                        " WHERE homeRole.id.homeId = :homeId"
        ),
        @NamedQuery(
                name = Constants.QUERY_FIN_USER_BY_HOME_ID_AND_USER_ID,
                query = "SELECT user FROM UserEntity user " +
                        "JOIN FETCH user.roles homeRole " +
                        " WHERE homeRole.id.homeId = :homeId AND homeRole.id.userId = :userId"
        )
})
@Table(name = "users")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String firstname;

    private String lastname;

    private String password;

    private String profilePictureUrl;

    private boolean vegetarian = false;

    private boolean verified = false;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<HomeRoleEntity> roles = new HashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TokenEntity> tokens = new HashSet<>();


    public UserEntity() {
        tokens = new HashSet<>();
    }


    public void addToken(TokenEntity token) {
        tokens.add(token);
        token.setUser(this);
    }

    public void removeToken(TokenEntity token) {
        tokens.remove(token);
        token.setUser(null);
    }
}
