package com.api.spring_security.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest implements Serializable {

    private String username;

    private String password;

}
