package com.api.spring_security.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUser implements Serializable {

    @Size(min = 4)
    private String name;

    @Size(min = 4)
    private String username;

    @Size(min = 8)
    private String password;

    @Size(min = 8)
    private String repeatedPassword;

}
