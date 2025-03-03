package org.univartois.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.dto.response.HomeRoleResponseDto;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.HomeRoleType;
import org.univartois.enums.Role;
import org.univartois.mapper.HomeRoleMapper;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.service.RoleService;

import java.util.*;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    HomeRoleRepository homeRoleRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    HomeRoleMapper homeRoleMapper;

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

    @Override
    public List<HomeRoleResponseDto> getHomeRoles() {
        return Arrays.stream(HomeRoleType.values()).map(homeRoleMapper::toHomeRoleResponseDto).toList();
    }
}
