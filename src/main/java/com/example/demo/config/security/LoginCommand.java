package com.example.demo.config.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class LoginCommand {

    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;
    private String token;
    private String name;
    private String lastName;
    private boolean enabled;
    private boolean admin;
}
