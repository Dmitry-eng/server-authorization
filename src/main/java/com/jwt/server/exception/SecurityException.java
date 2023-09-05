package com.jwt.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SecurityException extends RuntimeException {

    private String message;

}