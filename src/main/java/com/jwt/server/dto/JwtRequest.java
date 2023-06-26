package com.jwt.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
