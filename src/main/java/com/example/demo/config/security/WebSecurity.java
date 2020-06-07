package com.example.demo.config.security;

import com.example.demo.commons.security.AppUserDetailsService;
import com.example.demo.commons.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppUserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] publicEndpoints = new String[]{
                "/api/testEvent/**"
        };

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(publicEndpoints).permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userDetailsService, bCryptPasswordEncoder))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Not-Authenticated"));
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration x = new CorsConfiguration();
        x.applyPermitDefaultValues();
        x.addExposedHeader(SecurityConstants.HEADER_STRING);
        x.addAllowedMethod(HttpMethod.PUT.toString());
        x.addAllowedMethod(HttpMethod.DELETE.toString());
        source.registerCorsConfiguration("/**", x);
        return source;
    }
}
