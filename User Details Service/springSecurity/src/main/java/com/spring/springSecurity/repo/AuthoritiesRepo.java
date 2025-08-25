package com.spring.springSecurity.repo;

import com.spring.springSecurity.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepo extends JpaRepository<Authorities, Long> {
}
