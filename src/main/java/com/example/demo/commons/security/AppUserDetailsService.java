package com.example.demo.commons.security;

import com.example.demo.models.management.userlogin.Authority;
import com.example.demo.models.management.userlogin.Usuario;
import com.example.demo.repositories.management.userlogin.AuthorityRepository;
import com.example.demo.repositories.management.userlogin.UsuarioRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public LoginEntity loadUserByUsername(String s) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        if (user.isAdmin()) {
            List<Authority> allAuthorities = authorityRepository.findAll();
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (Authority authority : allAuthorities) {
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
            LoginEntity a = new LoginEntity(user.getUsername(), user.getPassword(), user.isAdmin(), authorities);
            a.setEnabled(user.isEnabled());
            a.setName(user.getName());
            a.setLastName(user.getLastName());
            return a;
        }

        LoginEntity a = new LoginEntity(user.getUsername(), user.getPassword(), user.isAdmin(), user.getAuthorities());
        a.setEnabled(user.isEnabled());
        a.setName(user.getName());
        a.setLastName(user.getLastName());
        return a;
    }
}
