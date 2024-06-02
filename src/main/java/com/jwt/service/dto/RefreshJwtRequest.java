package com.jwt.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RefreshJwtRequest {

    @NotBlank
    public String refreshToken;

}