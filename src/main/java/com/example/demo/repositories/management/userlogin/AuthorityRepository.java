package com.example.demo.repositories.management.userlogin;

import com.example.demo.models.management.userlogin.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    List<Authority> findAllByIdIn(List<Long> ids);
}
