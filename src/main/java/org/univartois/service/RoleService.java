package org.univartois.service;

import org.univartois.enums.Role;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface RoleService {
    boolean hasAnyRoleByHomeId(UUID homeId, Role... roles);

    Map<String, Set<String>> getRolesByUserId(UUID userId);
}
