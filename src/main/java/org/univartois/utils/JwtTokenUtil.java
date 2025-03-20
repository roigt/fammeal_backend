package org.univartois.utils;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.univartois.entity.UserEntity;
import org.univartois.enums.HomeRoleType;

import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class JwtTokenUtil {


    private final String issuer;

    private final static int DEFAULT_JWT_EXPIRES_DURATION = 3600 * 24; //    in secs (24 hours)

    public JwtTokenUtil(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer) {
        this.issuer = issuer;
    }


    public String generateJwtToken(final UserEntity user){
        final JsonObjectBuilder permissionsBuilder = Json.createObjectBuilder();
        user.getRoles().forEach(homeRole -> {
            final HomeRoleType roleInHome = homeRole.getRole();
            permissionsBuilder.add(
                    homeRole.getId().getHomeId().toString(),
                    Json.createObjectBuilder()
                            .add("name", roleInHome.name())
                            .add("value", roleInHome.getValue())
                            .build()
            );
        });

        return Jwt.issuer(issuer)
                .upn(user.getEmail())
                .claim(Claims.sub.name(), user.getId().toString())
                .claim(Claims.full_name.name(), user.getFirstname() + " " + user.getLastname())
                .claim(Claims.given_name.name(), user.getFirstname())
                .claim(Claims.family_name.name(), user.getLastname())
                .claim("roles", permissionsBuilder.build())
                .groups(Set.of())
                .issuedAt(Instant.now())
                .expiresIn(DEFAULT_JWT_EXPIRES_DURATION)
                .claim(Claims.email.name(), user.getEmail())
                .sign();

    }
}
