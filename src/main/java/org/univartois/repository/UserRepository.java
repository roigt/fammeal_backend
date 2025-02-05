package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.UserEntity;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

}
