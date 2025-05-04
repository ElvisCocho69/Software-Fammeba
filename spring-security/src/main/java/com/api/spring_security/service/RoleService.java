package com.api.spring_security.service;

import java.util.Optional;

import com.api.spring_security.persistence.entity.security.Role;

public interface RoleService {

    Optional<Role> findDefaultRole();

}
