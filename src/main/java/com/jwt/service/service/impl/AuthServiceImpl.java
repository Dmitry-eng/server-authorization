package com.jwt.service.service.impl;

import com.jwt.service.dto.JwtRequest;
import com.jwt.service.dto.Jwt;
import com.jwt.service.dto.Token;
import com.jwt.service.dto.authorization.Account;
import com.jwt.service.exception.SecurityException;
import com.jwt.service.service.AuthService;
import com.jwt.service.service.SecurityAccountService;
import com.jwt.service.service.cache.Cache;
import com.jwt.service.tool.JwtHelper;
import com.jwt.service.util.Constant;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static com.jwt.service.util.Constant.ACCOUNT_NOT_FOUND;
import static com.jwt.service.util.Constant.INCORRECT_PASSWORD;
import static com.jwt.service.util.Constant.INVALID_JWT_TOKEN;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse httpServletResponse;
    private final SecurityAccountService securityAccountService;
    private final Cache<Set<String>, String> accountCacheToken;

    @SneakyThrows
    @Override
    public void login(JwtRequest authRequest) {
        Account account = securityAccountService.findByLogin(authRequest.getLogin())
                .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));
        if (account.getPassword().equals(passwordEncoder.encode(authRequest.getPassword()))) {

            Jwt jwtResponse = jwtHelper.generateToken(account);
            addedTokenInCookie(jwtResponse);

            accountCacheToken.add(account.getLogin(), jwtResponse.getRefresh().getToken());
        } else {
            throw new SecurityException(INCORRECT_PASSWORD);
        }
    }

    @SneakyThrows
    @Override
    public void refresh(String refreshToken) {
        if (jwtHelper.validateRefreshToken(refreshToken)) {
            Claims claims = jwtHelper.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            Set<String> saveRefreshToken = accountCacheToken.get(login);
            if (saveRefreshToken.contains(refreshToken)) {
                Account account = securityAccountService.findByLogin(login)
                        .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));

                Jwt jwtResponse = jwtHelper.generateToken(account);
                addedTokenInCookie(jwtResponse);

                accountCacheToken.remove(account.getLogin(), refreshToken);
                accountCacheToken.add(account.getLogin(), jwtResponse.getRefresh().getToken());
            }
        }
        throw new SecurityException(INVALID_JWT_TOKEN);
    }


    private void addedTokenInCookie(Jwt response) {
        Cookie accessToken = new Cookie(Constant.ACCESS_TOKEN, response.getAccess().getToken());
        Cookie refreshToken = new Cookie(Constant.REFRESH_TOKEN, response.getRefresh().getToken());

        int expiredAccess = Optional.of(response)
                .map(Jwt::getAccess)
                .map(Token::getExpired)
                .map(Date::getTime)
                .map(Long::intValue)
                .orElse(-1);

        int expiredRefresh = Optional.of(response)
                .map(Jwt::getRefresh)
                .map(Token::getExpired)
                .map(Date::getTime)
                .map(Long::intValue)
                .orElse(-1);

        accessToken.setMaxAge(expiredAccess);
        refreshToken.setMaxAge(expiredRefresh);

        httpServletResponse.addCookie(accessToken);
        httpServletResponse.addCookie(refreshToken);
    }

}
