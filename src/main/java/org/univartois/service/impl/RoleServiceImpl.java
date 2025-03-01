package org.univartois.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.HomeRoleType;
import org.univartois.enums.Role;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.service.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    HomeRoleRepository homeRoleRepository;

    @Inject
    SecurityIdentity securityIdentity;

    //    @SuppressWarnings("unchecked")
    @Override
    public boolean hasAnyRoleByHomeId(UUID homeId, Role... roles) {
        String roleInHome = ((Map<String, String>) securityIdentity.getAttributes().get("roles")).getOrDefault(homeId.toString(), null);

        for (Role role : roles) {
            if (HomeRoleType.valueOf(roleInHome).includes(role)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Map<String, String> getRolesByUserId(UUID userId) {
        final List<HomeRoleEntity> roles = homeRoleRepository.findByUserId(userId);

        final Map<String, String> permissions = new HashMap<>();
        roles.forEach(homeRole -> {
            final String roleInHome = homeRole.getRole().toString();
            permissions.put(homeRole.getId().getHomeId().toString(), roleInHome);
        });
        return permissions;
    }
}
