package com.api.spring_security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.spring_security.dto.SaveRoleWithPermissions;
import com.api.spring_security.dto.ShowRoles;
import com.api.spring_security.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<ShowRoles>> getAllRoles(Pageable pageable) {
        try {
            Page<ShowRoles> roles = roleService.findAll(pageable); // Aquí se pasa pageable
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoleWithPermissions(
            @RequestBody @Valid SaveRoleWithPermissions saveRoleWithPermissions) {

        try {
            roleService.createRoleWithPermissions(saveRoleWithPermissions);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rol creado y permisos asignados.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el rol: " + e.getMessage());
        }

    }

}
