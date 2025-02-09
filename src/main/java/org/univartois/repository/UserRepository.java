package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.UserEntity;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public Optional<UserEntity> findByEmail(String email){
        return find("email = ?1",email).firstResultOptional();
    }


}
