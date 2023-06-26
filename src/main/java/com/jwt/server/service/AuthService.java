package com.jwt.server.service;

import com.jwt.server.dto.JwtRequest;
import com.jwt.server.dto.JwtResponse;
import lombok.NonNull;

public interface AuthService {

    JwtResponse login(@NonNull JwtRequest authRequest);

    JwtResponse refresh(@NonNull String refreshToken);

}
