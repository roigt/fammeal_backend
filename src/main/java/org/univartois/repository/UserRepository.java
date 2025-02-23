package org.univartois.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.univartois.entity.UserEntity;
import org.univartois.utils.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public Optional<UserEntity> findByEmail(String email) {
        return find("email = ?1", email).singleResultOptional();
    }

    public Optional<UserEntity> findById(UUID userId) {
        return find("id = ?1", userId).singleResultOptional();
    }

    public List<UserEntity> findUsersByHomeId(UUID homeId) {
        return list("#" + Constants.QUERY_FIND_USERS_BY_HOME_ID, Parameters.with("homeId", homeId));
    }

    public Optional<UserEntity> findUserByHomeIdAndUserId(UUID homeId, UUID userId) {
        return find("#" + Constants.QUERY_FIN_USER_BY_HOME_ID_AND_USER_ID, Parameters.with("homeId", homeId)
                .and("userId", userId)
        ).singleResultOptional();
    }

    public void updateProfilePictureByUserId(UUID userId, String imageUrl) {
        update("profilePictureUrl = ?1 WHERE id = ?2", imageUrl, userId);
    }

}
