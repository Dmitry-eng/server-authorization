package com.jwt.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
