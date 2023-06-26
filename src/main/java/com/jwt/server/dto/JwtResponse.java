package com.jwt.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

    @JsonProperty("type")
    private final String TYPE = "Bearer";

    private String accessToken;

    private String refreshToken;

}
