package org.univartois.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter @Setter
@Builder
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String firstname;

    private String lastname;

    private String password;

    private String salt;

    private String imageUrl;

    private boolean isVegetarian = false;

    private boolean isDarkModeEnabled = false;

    private boolean isVerified = false;

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TokenEntity> tokens = new HashSet<>();


    public UserEntity() {
        tokens = new HashSet<>();
    }


    public void addToken(TokenEntity token){
        tokens.add(token);
        token.setUser(this);
    }

    public void removeToken(TokenEntity token){
        tokens.remove(token);
        token.setUser(null);
    }
}
