package com.api.server.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.security.RegisteredUser;
import com.api.server.dto.security.SaveUser;
import com.api.server.dto.security.ChangePassword;
import com.api.server.service.auth.AuthenticationService;
import com.api.server.service.security.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired 
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<RegisteredUser>> listAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        try {
            Page<RegisteredUser> users = userService.findAll(role, status, pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PreAuthorize("permitAll")
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser) {
        RegisteredUser registeredUser = authenticationService.registerOneUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getUser(@PathVariable Long id) {
        RegisteredUser user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password")
    public ResponseEntity<RegisteredUser> changeOwnPassword(@RequestBody @Valid ChangePassword user) {
        RegisteredUser updatedUser = userService.changeOwnPassword(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('Administrador')")
    @PutMapping("/{id}/change-password")
    public ResponseEntity<RegisteredUser> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePassword user) {
        RegisteredUser updatedUser = userService.changePassword(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("permitAll")
    @PutMapping("/{id}/disable")
    public ResponseEntity<RegisteredUser> disableUser(@PathVariable Long id) {
        RegisteredUser updatedUser = userService.disableUser(id);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("permitAll")
    @PutMapping("/{id}")
    public ResponseEntity<RegisteredUser> updateUser(@PathVariable Long id, @RequestBody @Valid SaveUser user) {
        RegisteredUser updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
}
