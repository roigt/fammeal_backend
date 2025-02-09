package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.TokenEntity;
import org.univartois.enums.TokenType;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<TokenEntity> {

    public Optional<TokenEntity> findByTokenAndNotUsedAndNotExpired(String token) {
        return find("token = ?1 and used = ?2 and expiresAt > ?3", token, false, LocalDateTime.now()).firstResultOptional();
    }

    public void markUserTokensAsUsed(UUID userId, TokenType tokenType) {
        update("used = true WHERE user.id = ?1 AND tokenType = ?2", userId, tokenType);

    }


}
