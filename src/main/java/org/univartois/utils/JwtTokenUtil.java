package org.univartois.utils;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.univartois.entity.UserEntity;

import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class JwtTokenUtil {


    private final String issuer;

    private final static int DEFAULT_JWT_EXPIRES_DURATION = 3600; //    in secs (1 hour)

    public JwtTokenUtil(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer) {
        this.issuer = issuer;
    }


    public String generateJwtToken(final UserEntity user){
        final Map<String, String> permissions = new HashMap<>();
        user.getRoles().forEach(homeRole -> {
            final String roleInHome = homeRole.getRole().toString();
            permissions.put(homeRole.getId().getHomeId().toString(), roleInHome);
        });


        return Jwt.issuer(issuer)
                .upn(user.getEmail())
                .claim(Claims.sub.name(), user.getId().toString())
                .claim(Claims.full_name.name(), user.getFirstname() + " " + user.getLastname())
                .claim(Claims.given_name.name(), user.getFirstname())
                .claim(Claims.family_name.name(), user.getLastname())
                .claim("roles", permissions)
                .groups(Set.of())
                .issuedAt(Instant.now())
                .expiresIn(DEFAULT_JWT_EXPIRES_DURATION)
                .claim(Claims.email.name(), user.getEmail())
                .sign();

    }
}
