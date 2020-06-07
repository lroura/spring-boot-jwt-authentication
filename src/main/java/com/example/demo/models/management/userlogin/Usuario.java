package com.example.demo.models.management.userlogin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "gi_usuario",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uniqueUserConstraint",
                        columnNames = {"username"}
                )
        })
public class Usuario  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Builder.Default
    @JsonIgnore
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    @JsonIgnore
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    @JsonIgnore
    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Builder.Default
    private boolean admin = false;

    @Column(columnDefinition = "VARCHAR2(4000)")
    private String permisosAsignados;

    @Transient
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        if (permisosAsignados != null && !permisosAsignados.isEmpty()) {
            String[] split = permisosAsignados.split(",");
            List<SimpleGrantedAuthority> l = new ArrayList<>();
            for (String item : split) {
                l.add(new SimpleGrantedAuthority(item));
            }
            return l;
        }
        return new ArrayList<>();
    }

    @Transient
    public String getFullName() {
        return name + " " + lastName;
    }
}
