package org.univartois.utils;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Arrays;
import java.util.HashSet;

public class GenerateToken {

    public static String generateToken() {
        String token = Jwt.issuer("fammeal")
                .upn("reda@gmail.com")
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.birthdate.name(), "2006-07-13")
                .sign();
        return token;
    }
}
