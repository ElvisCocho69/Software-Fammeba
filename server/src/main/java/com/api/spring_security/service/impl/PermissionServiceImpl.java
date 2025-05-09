package com.api.spring_security.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.spring_security.dto.SavePermission;
import com.api.spring_security.dto.ShowPermission;
import com.api.spring_security.exception.ObjectNotFoundException;
import com.api.spring_security.persistence.entity.security.GrantedPermission;
import com.api.spring_security.persistence.entity.security.Operation;
import com.api.spring_security.persistence.entity.security.Role;
import com.api.spring_security.persistence.repository.security.OperationRepository;
import com.api.spring_security.persistence.repository.security.PermissionRepository;
import com.api.spring_security.persistence.repository.security.RoleRepository;
import com.api.spring_security.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<ShowPermission> findAll(Pageable pageable) {

        return permissionRepository.findAll(pageable)
                .map(this::mapEntityToShowDto);

    }

    private ShowPermission mapEntityToShowDto(GrantedPermission grantedPermission) {

        if (grantedPermission == null)
            return null;

        ShowPermission showDto = new ShowPermission();
        showDto.setId(grantedPermission.getId());
        showDto.setRole(grantedPermission.getRole().getName());
        showDto.setOperation(grantedPermission.getOperation().getName());
        showDto.setHttpMethod(grantedPermission.getOperation().getHttpMethod());
        showDto.setModule(grantedPermission.getOperation().getModule().getName());

        return showDto;

    }

    @Override
    public Optional<ShowPermission> findOneById(Long permissionId) {

        return permissionRepository.findById(permissionId)
                .map(this::mapEntityToShowDto);

    }

    @Override
    public ShowPermission createOne(SavePermission savePermission) {

        GrantedPermission newPermission = new GrantedPermission();

        Operation operation = operationRepository.findByName(savePermission.getOperation()).orElseThrow(
                () -> new ObjectNotFoundException("Operation not found. Opeation: " + savePermission.getOperation()));

        newPermission.setOperation(operation);

        Role role = roleRepository.findByName(savePermission.getRole()).orElseThrow(
                () -> new ObjectNotFoundException("Role not found. Opeation: " + savePermission.getRole()));

        newPermission.setRole(role);

        permissionRepository.save(newPermission);

        return this.mapEntityToShowDto(newPermission);

    }

    @Override
    public ShowPermission deleteOneById(Long permissionId) {

        GrantedPermission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ObjectNotFoundException("Permission not found. Permission: " + permissionId));

        permissionRepository.delete(permission);

        return this.mapEntityToShowDto(permission);

    }

}
