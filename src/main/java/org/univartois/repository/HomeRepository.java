package org.univartois.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.HomeEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class HomeRepository implements PanacheRepository<HomeEntity> {
    public Optional<HomeEntity> findHomeById(UUID homeId){
        return find("id = ?1", homeId).firstResultOptional();
    }
}
