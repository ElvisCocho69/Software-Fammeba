package com.api.spring_security.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavePermission implements Serializable {

    @NotBlank
    private String role;

    @NotBlank
    private String operation;

}
