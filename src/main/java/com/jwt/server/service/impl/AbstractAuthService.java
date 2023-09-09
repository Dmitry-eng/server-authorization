package com.jwt.server.service.impl;

import com.jwt.server.dto.JwtRequest;
import com.jwt.server.dto.JwtResponse;
import com.jwt.server.dto.authorization.Authorization;
import com.jwt.server.exception.SecurityException;
import com.jwt.server.service.AuthService;
import com.jwt.server.service.cache.Cache;
import com.jwt.server.tool.JwtHelper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static com.jwt.server.config.filter.Constant.ACCOUNT_NOT_FOUND;
import static com.jwt.server.config.filter.Constant.INCORRECT_PASSWORD;
import static com.jwt.server.config.filter.Constant.INVALID_JWT_TOKEN;


public abstract class AbstractAuthService implements AuthService {
    @Autowired
    protected JwtHelper jwtHelper;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    @SneakyThrows
    @Override
    public JwtResponse login(JwtRequest authRequest) {

        Authorization authorization = getAccount(authRequest.getLogin())
                .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));

        if (authorization.getPassword().equals(passwordEncoder.encode(authRequest.getPassword()))) {
            JwtResponse jwtResponse = generateToken(authorization);
            getCache().add(authorization.getLogin(), jwtResponse.getRefreshToken());
            return jwtResponse;
        } else {
            throw new SecurityException(INCORRECT_PASSWORD);
        }
    }

    @SneakyThrows
    @Override
    public JwtResponse refresh(String refreshToken) {
        if (jwtHelper.validateRefreshToken(refreshToken)) {
            Claims claims = jwtHelper.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            Set<String> saveRefreshToken = getCache().get(login);
            if (saveRefreshToken.contains(refreshToken)) {
                Authorization authorization = getAccount(login)
                        .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));

                JwtResponse jwtResponse = generateToken(authorization);

                getCache().remove(authorization.getLogin(), refreshToken);
                getCache().add(authorization.getLogin(), jwtResponse.getRefreshToken());
                return jwtResponse;
            }
        }
            throw new SecurityException(INVALID_JWT_TOKEN);
    }

    @Override
    public void logout() {

    }

    protected JwtResponse generateToken(Authorization authorization) {
        String accessToken = jwtHelper.generateAccessToken(authorization);
        String newRefreshToken = jwtHelper.generateRefreshToken(authorization);
        return new JwtResponse(accessToken, newRefreshToken);
    }

    protected abstract Optional<Authorization> getAccount(String login);

    protected abstract Cache<Set<String>, String> getCache();
}
