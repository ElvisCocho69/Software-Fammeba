package com.api.spring_security.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisteredUser implements Serializable {

    private Long id;

    private String username;
    
    private String name;

    private String lastname;

    private String email;

    private String contacto;

    private String status;

    private String role;

    private String jwt;

}
