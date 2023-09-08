package com.jwt.server.controller;

import com.jwt.server.dto.JwtRequest;
import com.jwt.server.dto.JwtResponse;
import com.jwt.server.dto.RefreshJwtRequest;
import com.jwt.server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/auth")
@RequiredArgsConstructor
public class AuthAccountController {

    private final AuthService authAccount;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid JwtRequest authRequest) {
            JwtResponse token = authAccount.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewTokenByRefresh(@RequestBody @Valid RefreshJwtRequest request) {
        JwtResponse token = authAccount.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
