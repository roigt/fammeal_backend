package org.univartois.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.HomeRoleType;
import org.univartois.enums.Role;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.service.RoleService;

import java.util.*;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    HomeRoleRepository homeRoleRepository;

    @Inject
    SecurityIdentity securityIdentity;


    //    @SuppressWarnings("unchecked")
    @Override
    public boolean hasAnyRoleByHomeId(UUID homeId, Role... roles) {
        HomeRoleType roleInHome = getCurrentAuthUserRolesFromJwt().getOrDefault(homeId.toString(), null);

        if (roleInHome == null) return false;
        for (Role role : roles) {
            if (roleInHome.includes(role)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public Map<String, HomeRoleType> getRolesByUserId(UUID userId) {
        final Map<String, HomeRoleType> permissions = new HashMap<>();

        final List<HomeRoleEntity> roles = homeRoleRepository.findByUserId(userId);

        roles.forEach(homeRole -> {
            final HomeRoleType roleInHome = homeRole.getRole();
            permissions.put(homeRole.getId().getHomeId().toString(), roleInHome);
        });
        return permissions;
    }

    @Override
    public Map<String, HomeRoleType> getCurrentAuthUserRolesFromJwt() {
        return (Map<String, HomeRoleType>) securityIdentity.getAttributes().getOrDefault("roles", new HashMap<>());
    }

    @Override
    public List<HomeRoleType> getHomeRoles() {
        return Arrays.stream(HomeRoleType.values()).toList();
    }
}
