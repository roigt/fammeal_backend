package org.univartois.service;

import org.univartois.enums.HomeRoleType;
import org.univartois.enums.Role;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RoleService {
    boolean hasAnyRoleByHomeId(UUID homeId, Role... roles);
    Map<String, List<String>> getRolesByUserId(UUID userId);
}
