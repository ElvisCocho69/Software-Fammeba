package com.api.spring_security.persistence.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.spring_security.persistence.entity.security.GrantedPermission;

public interface PermissionRepository extends JpaRepository<GrantedPermission, Long> {

}
