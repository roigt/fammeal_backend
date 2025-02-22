package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.HomeRoleType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class HomeRoleRepository implements PanacheRepository<HomeRoleEntity> {
    public List<HomeRoleEntity> findByUserId(UUID userId){
        return list("user.id = ?1", userId);
    }

    public void deleteByUserIdAndHomeId(UUID userId, UUID homeId){
        delete("id.userId = ?1 AND id.homeId = ?2", userId, homeId);
    }

    public long countAdminRolesByHomeId(UUID homeId){
        return count("id.homeId = ?1 AND role = ?2", homeId, HomeRoleType.ADMIN);
    }

    public boolean existsByUserIdAndHomeId(UUID userId, UUID homeId){
        return count("id.userId = ?1 AND id.homeId = ?2", userId, homeId) > 0;
    }




    public void assignRoleToUserInHome(UUID homeId, UUID userId, HomeRoleType role){

    }
}
