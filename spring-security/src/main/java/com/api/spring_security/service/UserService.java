package com.api.spring_security.service;

import java.util.Optional;

import com.api.spring_security.dto.SaveUser;
import com.api.spring_security.persistence.entity.security.User;

public interface UserService {

    User registerOneCostumer(SaveUser newUser);
 
    Optional<User> findOneByUsername(String username);

}
