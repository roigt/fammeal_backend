package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.HomeEntity;
import org.univartois.utils.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class HomeRepository implements PanacheRepository<HomeEntity> {


    public Optional<HomeEntity> findById(UUID id) {
        return find("id = ?1", id).firstResultOptional();
    }

    public List<HomeEntity> findHomesByUserId(UUID userId) {
        return list("#" + Constants.QUERY_FIND_HOMES_BY_USER_ID, Parameters.with("userId", userId));
    }
}