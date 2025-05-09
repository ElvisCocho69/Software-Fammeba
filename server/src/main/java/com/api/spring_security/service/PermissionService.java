package com.api.spring_security.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.spring_security.dto.SavePermission;
import com.api.spring_security.dto.ShowPermission;

public interface PermissionService {

    Page<ShowPermission> findAll(Pageable pageable);

    Optional<ShowPermission> findOneById(Long permissionId);

    ShowPermission createOne(SavePermission savePermission);

    ShowPermission deleteOneById(Long permissionId);

}
