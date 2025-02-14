package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public Optional<UserEntity> findByEmail(String email){
        return find("email = ?1",email).firstResultOptional();
    }

    public Optional<UserEntity> findById(UUID userId){
        return find("id = ?1", userId).firstResultOptional();
    }


}
