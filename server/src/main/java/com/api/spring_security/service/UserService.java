package com.api.spring_security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.spring_security.dto.RegisteredUser;
import com.api.spring_security.dto.SaveUser;
import com.api.spring_security.persistence.entity.security.User;

public interface UserService {

    Page<RegisteredUser> findAll(String role, String status, Pageable pageable);

    User registerOneUser(SaveUser newUser);
 
    Optional<User> findOneByUsername(String username);

}
