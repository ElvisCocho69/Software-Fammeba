package com.api.spring_security.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public User registerOneCostumer(SaveUser newUser) {
        
        validatePassword(newUser);

        User user = new User();

        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        
        Role defaultRole = roleService.findDefaultRole()
            .orElseThrow(() -> new ObjectNotFoundException("Role not found. Default Role"));

        user.setRole(defaultRole);

        return userRepository.save(user);

    }

    private void validatePassword(SaveUser dto) {

        if(!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Password don't match");
        }

        if(!dto.getPassword().equals(dto.getRepeatedPassword())) {
            throw new InvalidPasswordException("Password don't match");
        }

    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        
        return userRepository.findByUsername(username);

    }

}   
