package org.univartois.service;

import org.univartois.dto.response.HomeRoleResponseDto;
import org.univartois.enums.Role;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RoleService {
    boolean hasAnyRoleByHomeId(UUID homeId, Role... roles);

    Map<String, String> getRolesByUserId(UUID userId);

    List<HomeRoleResponseDto> getHomeRoles();
}
