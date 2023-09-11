package com.jwt.server.service.impl;

import com.jwt.server.dto.JwtRequest;
import com.jwt.server.dto.JwtResponse;
import com.jwt.server.dto.authorization.Authorization;
import com.jwt.server.exception.SecurityException;
import com.jwt.server.service.AuthService;
import com.jwt.server.service.SecurityAccountRepository;
import com.jwt.server.service.cache.Cache;
import com.jwt.server.tool.JwtHelper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.jwt.server.config.filter.Constant.ACCOUNT_NOT_FOUND;
import static com.jwt.server.config.filter.Constant.INCORRECT_PASSWORD;
import static com.jwt.server.config.filter.Constant.INVALID_JWT_TOKEN;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityAccountRepository securityAccountRepository;
    private final Cache<Set<String>, String> cache;

    @SneakyThrows
    @Override
    public JwtResponse login(JwtRequest authRequest) {

        Authorization authorization = securityAccountRepository.findByLogin(authRequest.getLogin())
                .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));

        if (authorization.getPassword().equals(passwordEncoder.encode(authRequest.getPassword()))) {
            JwtResponse jwtResponse = generateToken(authorization);
            cache.add(authorization.getLogin(), jwtResponse.getRefreshToken());
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
            Set<String> saveRefreshToken = cache.get(login);
            if (saveRefreshToken.contains(refreshToken)) {
                Authorization authorization = securityAccountRepository.findByLogin(login)
                        .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));
                JwtResponse jwtResponse = generateToken(authorization);

                cache.remove(authorization.getLogin(), refreshToken);
                cache.add(authorization.getLogin(), jwtResponse.getRefreshToken());
                return jwtResponse;
            }
        }
        throw new SecurityException(INVALID_JWT_TOKEN);
    }

    protected JwtResponse generateToken(Authorization authorization) {
        String accessToken = jwtHelper.generateAccessToken(authorization);
        String newRefreshToken = jwtHelper.generateRefreshToken(authorization);
        return new JwtResponse(accessToken, newRefreshToken);
    }
}
