package org.univartois.utils;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.univartois.entity.UserEntity;

import java.util.*;

@ApplicationScoped
public class JwtTokenUtil {


    private final String issuer;

    public JwtTokenUtil(@ConfigProperty(name = "mp.jwt.verify.issuer") String issuer) {
        this.issuer = issuer;
    }


    public String generateJwtToken(final UserEntity user){
        final Map<String, List<String>> permissions = new HashMap<>();
        user.getRoles().forEach(homeRole -> {
            final List<String> homeRoles = permissions.getOrDefault(homeRole.getId().getHomeId().toString(), new ArrayList<>());
            homeRoles.add(homeRole.getRole().name());
            permissions.put(homeRole.getId().getHomeId().toString(), homeRoles);
        });

        permissions.put(UUID.randomUUID().toString(), List.of("ADMIN"));

        return Jwt.issuer(issuer)
                .upn(user.getEmail())
                .claim(Claims.sub.name(), user.getId().toString())
                .claim(Claims.full_name.name(), user.getFirstname() + " " + user.getLastname())
                .claim(Claims.given_name.name(), user.getFirstname())
                .claim(Claims.family_name.name(), user.getLastname())
                .claim("permissions", permissions)
                .groups(Set.of())
                .issuedAt(System.currentTimeMillis())
                .expiresIn(3600)
                .claim(Claims.email.name(), user.getEmail())
                .sign();

    }
}
