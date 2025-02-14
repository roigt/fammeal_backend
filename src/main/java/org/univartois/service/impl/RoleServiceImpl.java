package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.Role;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.RoleService;

import java.util.*;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {
    @Inject
    UserRepository userRepository;

    @Inject
    HomeRoleRepository homeRoleRepository;

    @Override
    public boolean hasAnyRoleByHomeId(UUID homeId, Role... roles) {
        return false;
    }


    @Override
    public Map<String, List<String>> getRolesByUserId(UUID userId) {
        final List<HomeRoleEntity> roles = homeRoleRepository.findHomeRolesByUserId(userId);

        final Map<String, List<String>> permissions = new HashMap<>();
        roles.forEach(homeRole -> {
            final List<String> homeRoles = permissions.getOrDefault(homeRole.getId().getHomeId().toString(), new ArrayList<>());
            homeRoles.add(homeRole.getRole().name());
            permissions.put(homeRole.getId().getHomeId().toString(), homeRoles);
        });
        permissions.put(UUID.randomUUID().toString(), List.of("ADMIN"));
        return permissions;
    }
}
