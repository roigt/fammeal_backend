package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.TokenEntity;
import org.univartois.enums.TokenType;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<TokenEntity> {

    public Optional<TokenEntity> findValidToken(String token, TokenType tokenType) {
        return find(
                "token = :token AND used = :unused AND expiresAt > :now AND tokenType = :type",
                Parameters.with("token", token)
                        .and("unused", false)
                        .and("now", LocalDateTime.now())
                        .and("type", tokenType)
        ).firstResultOptional();
    }

    public void markUserTokensAsUsed(UUID userId, TokenType tokenType) {
        update("used = true WHERE user.id = ?1 AND tokenType = ?2", userId, tokenType);

    }


}
