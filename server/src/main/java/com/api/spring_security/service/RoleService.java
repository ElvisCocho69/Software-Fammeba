package com.api.spring_security.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.spring_security.dto.SaveRoleWithPermissions;
import com.api.spring_security.dto.ShowRoles;
import com.api.spring_security.persistence.entity.security.Role;

public interface RoleService {

    Optional<Role> findDefaultRole();

    Page<ShowRoles> findAll(Pageable pageable);

    void createRoleWithPermissions(SaveRoleWithPermissions saveRoleWithPermissions);
    
    void updateRoleWithPermissions(Long roleId, SaveRoleWithPermissions updateRoleRequest);
    
    void deleteRoleById(Long roleId);

    Optional<Role> findByName(String role);
}
