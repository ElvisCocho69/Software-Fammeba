package com.api.spring_security.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowPermission implements Serializable {

    private Long id;

    private String operation;

    private String httpMethod;

    private String module;
    
    private String role;

}
