package com.example.demo.config.security;

import com.example.demo.commons.security.AppUserDetailsService;
import com.example.demo.commons.security.LoginEntity;
import com.example.demo.commons.security.SecurityConstants;
import com.example.demo.commons.security.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService appUserDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    Gson gson = new GsonBuilder().setExclusionStrategies(new UserExclusions())
            .create();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AppUserDetailsService appUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            LoginCommand creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginCommand.class);

            String token = SecurityUtil.generateToken(creds.getUsername());
            creds.setToken(SecurityConstants.TOKEN_PREFIX + token);

            LoginEntity ud = appUserDetailsService.loadUserByUsername(creds.getUsername());

            if (!bCryptPasswordEncoder.matches(creds.getPassword(), ud.getPassword())) {
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                return null;
            }
            if (!ud.isEnabled()) {
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                return null;
            }

            creds.setAuthorities(ud.getAuthorities());
            creds.setLastName(ud.getLastName());
            creds.setName(ud.getName());
            creds.setEnabled(ud.isEnabled());
            creds.setAdmin(ud.isAdmin());

            String userString = this.gson.toJson(creds);

            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(userString);
            out.flush();

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        String token = SecurityUtil.generateToken(((User) auth.getPrincipal()).getUsername());
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }

    static class UserExclusions implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getDeclaringClass() == LoginCommand.class && f.getName().equals("password"));
        }
    }
}
