package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.HomeRoleEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HomeRoleRepository implements PanacheRepository<HomeRoleEntity> {
    public List<HomeRoleEntity> findHomeRolesByUserId(UUID userId){
        return list("user.id = ?1", userId);
    }

}
