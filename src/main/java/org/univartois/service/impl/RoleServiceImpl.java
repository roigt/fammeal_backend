package org.univartois.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.HomeRoleType;
import org.univartois.enums.Role;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.RoleService;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {
    @Inject
    UserRepository userRepository;

    @Inject
    HomeRoleRepository homeRoleRepository;

    @Inject
    SecurityIdentity securityIdentity;

//    @SuppressWarnings("unchecked")
    @Override
    public boolean hasAnyRoleByHomeId(UUID homeId, Role... roles) {
        Set<HomeRoleType> homeRoles = ((Map<String, Set<String>>)securityIdentity.getAttributes().get("roles")).getOrDefault(homeId.toString(), Set.of()).stream().map(HomeRoleType::valueOf).collect(Collectors.toUnmodifiableSet());

        for (Role role : roles) {
            if (homeRoles.stream().anyMatch(homeRole -> homeRole.includes(role))){
                return true;
            }
        }

        return false;
    }


    @Override
    public Map<String, Set<String>> getRolesByUserId(UUID userId) {
        final List<HomeRoleEntity> roles = homeRoleRepository.findByUserId(userId);

        final Map<String, Set<String>> permissions = new HashMap<>();
        roles.forEach(homeRole -> {
            final Set<String> homeRoles = permissions.getOrDefault(homeRole.getId().getHomeId().toString(), new HashSet<>());
            homeRoles.add(homeRole.getRole().name());
            permissions.put(homeRole.getId().getHomeId().toString(), homeRoles);
        });
        return permissions;
    }
}
