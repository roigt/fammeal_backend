package org.univartois.security;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.univartois.enums.HomeRoleType;
import org.univartois.service.RoleService;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
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

        final Map<String, HomeRoleType> permissions = roleService.getRolesByUserId(UUID.fromString(user.getSubject()));

        builder.addAttributes(Collections.singletonMap("roles", permissions));
        return builder.build();
    }
}
