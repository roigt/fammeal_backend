package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.univartois.enums.TokenType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TokenEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private String token;

    private LocalDateTime expiresAt;

    private boolean used = false;

//    we have to put EAGER because of hibernate proxies cant apply SqlRestriction
    @NotFound(action = NotFoundAction.EXCEPTION)
    @ManyToOne
    private UserEntity user;


}
