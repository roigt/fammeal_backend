package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;


}
