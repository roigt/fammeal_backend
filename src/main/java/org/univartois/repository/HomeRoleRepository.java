package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.entity.id.HomeRoleId;
import org.univartois.enums.HomeRoleType;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HomeRoleRepository implements PanacheRepositoryBase<HomeRoleEntity, HomeRoleId> {
    public List<HomeRoleEntity> findByUserId(UUID userId) {
        return list("user.id = ?1", userId);
    }

    public long deleteById(UUID homeId, UUID userId){
        return delete("id.homeId = ?1 AND id.userId = ?2", homeId, userId);
    }

    public boolean existsByUserIdAndHomeId(UUID userId, UUID homeId) {
        return count("id.userId = ?1 AND id.homeId = ?2", userId, homeId) > 0;
    }



}
