package com.example.demo.commons.security;

import com.auth0.jwt.JWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class SecurityUtil {

    /**
     * Devuelve el key del usuario autenticado
     *
     * @return String
     */
    public static String authenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "Self-app";
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof String) {
                username = (String) principal;
            }
            if (principal instanceof User) {
                username = ((User) principal).getUsername();
            }
        }
        return username;
    }

    /**
     * Genera un token de autenticacion
     *
     * @param username
     * @return
     */
    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
    }
}
