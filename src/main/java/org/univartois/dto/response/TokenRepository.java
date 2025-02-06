package org.univartois.dto.response;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.TokenEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class TokenRepository implements PanacheRepository<TokenEntity> {

    public Optional<TokenEntity> findByTokenAndNotUsedAndNotExpired(String token) {
        return find("token = ?1 and used = ?2 and expiresAt > ?3", token, false, LocalDateTime.now()).firstResultOptional();
    }
}
