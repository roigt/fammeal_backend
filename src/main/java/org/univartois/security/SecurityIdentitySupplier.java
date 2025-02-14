package org.univartois.security;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.service.RoleService;

import java.security.Principal;
import java.util.*;
import java.util.function.Supplier;

@Dependent
public class SecurityIdentitySupplier implements Supplier<SecurityIdentity> {
    @Setter
    private SecurityIdentity identity;


    @Inject
    RoleService roleService;

    @Transactional
    @Override
    public SecurityIdentity get() {
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);

        JsonWebToken user = (JsonWebToken) identity.getPrincipal();

        final Map<String, List<String>> permissions = roleService.getRolesByUserId(UUID.fromString(user.getSubject()));

        builder.addAttributes(Collections.singletonMap("permissions", permissions));
        return builder.build();
    }
}
