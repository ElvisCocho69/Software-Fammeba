package com.api.spring_security.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.spring_security.dto.SaveRoleWithPermissions;
import com.api.spring_security.dto.ShowRoles;
import com.api.spring_security.persistence.entity.security.GrantedPermission;
import com.api.spring_security.persistence.entity.security.Operation;
import com.api.spring_security.persistence.entity.security.Role;
import com.api.spring_security.persistence.repository.security.OperationRepository;
import com.api.spring_security.persistence.repository.security.PermissionRepository;
import com.api.spring_security.persistence.repository.security.RoleRepository;
import com.api.spring_security.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Value("${security.default.role}")
    private String defaultRole;

    @Override
    public Optional<Role> findDefaultRole() {

        return roleRepository.findByName(defaultRole);

    }

    @Override
    public Page<ShowRoles> findAll(Pageable pageable) {
        // Obtener los roles paginados desde el repositorio
        return roleRepository.findAll(pageable).map(this::mapRoleToShowDto);
    }

    // Método para mapear un role a un DTO
    private ShowRoles mapRoleToShowDto(Role role) {
        ShowRoles showDto = new ShowRoles();
        showDto.setId(role.getId());
        showDto.setName(role.getName());

        // Extraer permisos relacionados con el rol
        List<String> permissions = role.getPermissions().stream()
                .map(grantedPermission -> grantedPermission.getOperation().getName()) // Obtener el nombre de la operación
                .collect(Collectors.toList());

        showDto.setPermissions(permissions);
        return showDto;
    }

    @Override
    public void createRoleWithPermissions(SaveRoleWithPermissions saveRoleWithPermissions) {

        Role role = new Role();

        role.setName(saveRoleWithPermissions.getName());

        roleRepository.save(role);

        for (Long operationId : saveRoleWithPermissions.getOperationIds()) {
            Operation operation = operationRepository.findById(operationId)
                    .orElseThrow(() -> new RuntimeException("Operación no encontrada. ID: " + operationId));

            // Asocia el permiso con el rol
            GrantedPermission grantedPermission = new GrantedPermission();
            grantedPermission.setRole(role);
            grantedPermission.setOperation(operation);

            permissionRepository.save(grantedPermission);
        }

    }

}
