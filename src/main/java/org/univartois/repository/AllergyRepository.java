package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.AllergyEntity;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class AllergyRepository implements PanacheRepositoryBase<AllergyEntity, Long> {

    public List<AllergyEntity> findByIds(Set<Long> ids){
        return list("id IN ?1", ids);
    }
}
