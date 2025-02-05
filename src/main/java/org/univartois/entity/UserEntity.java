package org.univartois.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String password;

    private String salt;

    private String imageUrl;

    private boolean isVegetarian = false;

    private boolean isDarkModeEnabled = false;

    private boolean isVerified = false;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<TokenEntity> tokens = new HashSet<>();


}
