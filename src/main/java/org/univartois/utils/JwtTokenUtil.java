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
        final Map<String, Set<String>> permissions = new HashMap<>();
        user.getRoles().forEach(homeRole -> {
            final Set<String> homeRoles = permissions.getOrDefault(homeRole.getId().getHomeId().toString(), new HashSet<>());
            homeRoles.add(homeRole.getRole().name());
            permissions.put(homeRole.getId().getHomeId().toString(), homeRoles);
        });


        return Jwt.issuer(issuer)
                .upn(user.getEmail())
                .claim(Claims.sub.name(), user.getId().toString())
                .claim(Claims.full_name.name(), user.getFirstname() + " " + user.getLastname())
                .claim(Claims.given_name.name(), user.getFirstname())
                .claim(Claims.family_name.name(), user.getLastname())
                .claim("roles", permissions)
                .groups(Set.of())
                .issuedAt(System.currentTimeMillis())
                .expiresIn(3600)
                .claim(Claims.email.name(), user.getEmail())
                .sign();

    }
}
