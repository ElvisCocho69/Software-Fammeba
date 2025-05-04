package com.api.spring_security.persistence.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.spring_security.persistence.entity.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String defaultRole);

}
