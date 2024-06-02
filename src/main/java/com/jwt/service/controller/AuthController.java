package com.jwt.service.controller;

import com.jwt.service.dto.JwtRequest;
import com.jwt.service.dto.RefreshJwtRequest;
import com.jwt.service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authAccount;

    @PostMapping("/login")
    public void login(@RequestBody @Valid JwtRequest authRequest) {
        authAccount.login(authRequest);
    }

    @PostMapping("/refresh")
    public void getNewTokenByRefresh(@RequestBody @Valid RefreshJwtRequest request) {
        authAccount.refresh(request.getRefreshToken());
    }
}
