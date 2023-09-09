package com.jwt.server.service;

import com.jwt.server.dto.JwtRequest;
import com.jwt.server.dto.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest authRequest);

    JwtResponse refresh(String refreshToken);

    void logout();

}
