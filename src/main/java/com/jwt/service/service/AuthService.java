package com.jwt.service.service;

import com.jwt.service.dto.JwtRequest;

public interface AuthService {

    void login(JwtRequest authRequest);

    void refresh(String refreshToken);

}
