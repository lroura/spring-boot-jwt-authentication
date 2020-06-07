package com.example.demo.repositories.management.userlogin;

import com.example.demo.models.management.userlogin.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    Usuario findByUsernameAndIdNot(String username, Long id);

}
