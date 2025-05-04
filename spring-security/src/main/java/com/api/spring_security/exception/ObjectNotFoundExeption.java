package com.api.spring_security.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ObjectNotFoundExeption extends RuntimeException {

    public ObjectNotFoundExeption(String message) {
        super(message);
    }

    public ObjectNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }  

}
