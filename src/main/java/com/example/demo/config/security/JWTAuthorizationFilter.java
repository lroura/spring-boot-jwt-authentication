package com.example.demo.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.commons.security.AppUserDetailsService;
import com.example.demo.commons.security.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final AppUserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, AppUserDetailsService userDetailsService) {
        super(authManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null) {
            try {
                String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                        .getSubject();
                UserDetails ud = userDetailsService.loadUserByUsername(user);

                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, ud.getAuthorities());
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
