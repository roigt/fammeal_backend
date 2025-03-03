package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.UserEntity;
import org.univartois.utils.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public Optional<UserEntity> findByEmail(String email) {
        return find("email = ?1", email).singleResultOptional();
    }
    
    public List<UserEntity> findUsersByHomeId(UUID homeId) {
        return list("#" + Constants.QUERY_FIND_USERS_BY_HOME_ID, Parameters.with("homeId", homeId));
    }

    public boolean existsByUsernameAndNotId(String username, UUID id){
        return count("id != :id AND username = :username", Parameters.with("id", id).and("username", username)) > 0;
    }

    public Optional<UserEntity> findUserByHomeIdAndUserId(UUID homeId, UUID userId) {
        return find("#" + Constants.QUERY_FIND_USER_BY_HOME_ID_AND_USER_ID, Parameters.with("homeId", homeId)
                .and("userId", userId)
        ).singleResultOptional();
    }

    public void updateProfilePictureByUserId(UUID userId, String imageUrl) {
        update("profilePictureUrl = ?1 WHERE id = ?2", imageUrl, userId);
    }

}
