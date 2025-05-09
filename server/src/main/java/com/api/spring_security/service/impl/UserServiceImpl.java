package com.api.spring_security.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.api.spring_security.dto.RegisteredUser;
import com.api.spring_security.dto.SaveUser;
import com.api.spring_security.exception.InvalidPasswordException;
import com.api.spring_security.exception.ObjectNotFoundException;
import com.api.spring_security.persistence.entity.security.Role;
import com.api.spring_security.persistence.entity.security.User;
import com.api.spring_security.persistence.repository.security.UserRepository;
import com.api.spring_security.service.RoleService;
import com.api.spring_security.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public Page<RegisteredUser> findAll(String role, String status, Pageable pageable) {
        Page<User> users;
        
        if (role != null && status != null) {
            users = userRepository.findByRoleNameAndStatus(role, User.UserStatus.valueOf(status), pageable);
        } else if (role != null) {
            users = userRepository.findByRoleName(role, pageable);
        } else if (status != null) {
            users = userRepository.findByStatus(User.UserStatus.valueOf(status), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        
        return users.map(user -> {
            RegisteredUser dto = new RegisteredUser();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setLastname(user.getLastname());
            dto.setEmail(user.getEmail());
            dto.setContacto(user.getContacto());
            dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
            dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
            return dto;
        });
    }

    @Override
    public User registerOneUser(SaveUser newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        user.setContacto(newUser.getContacto());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setStatus(User.UserStatus.valueOf(newUser.getStatus()));

        Role role = null;
        if (newUser.getRole() != null && !newUser.getRole().isEmpty()) {
            role = roleService.findByName(newUser.getRole())
                    .orElseThrow(() -> new ObjectNotFoundException("Role not found: " + newUser.getRole()));
        } else {
            role = roleService.findDefaultRole()
                    .orElseThrow(() -> new ObjectNotFoundException("Role not found. Default Role"));
        }
        user.setRole(role);

        return userRepository.save(user);
    }

    private void validatePassword(SaveUser dto) {
        if (!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Password don't match");
        }

        if (!dto.getPassword().equals(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Password don't match");
        }
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
