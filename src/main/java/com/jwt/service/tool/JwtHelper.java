package com.jwt.service.tool;

import com.jwt.service.dto.ExpirationTime;
import com.jwt.service.dto.Jwt;
import com.jwt.service.dto.Token;
import com.jwt.service.dto.authorization.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.jwt.service.util.Constant.HASH_PASSWORD;

@RequiredArgsConstructor
public class JwtHelper {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final ExpirationTime accessExpirationTime;
    private final ExpirationTime refreshExpirationTime;

    public Jwt generateToken(Account account) {

        Date expiredAccessToken = getExpirationDate(accessExpirationTime);
        Date expiredRefreshToken = getExpirationDate(refreshExpirationTime);

        String accessToken = generateAccessToken(account, expiredAccessToken);
        String refreshToken = generateRefreshToken(account, expiredRefreshToken);

        Token access = new Token(accessToken, expiredAccessToken);
        Token refresh = new Token(refreshToken, expiredRefreshToken);

        return new Jwt(access, refresh);
    }

    public String generateAccessToken(Account account, Date expited) {
        return Jwts.builder()
                .setSubject(account.getLogin())
                .setExpiration(expited)
                .signWith(jwtAccessSecret)
                .claim(HASH_PASSWORD, account.getPassword())
                .compact();
    }

    public String generateRefreshToken(Account account, Date expired) {
        return Jwts.builder()
                .setSubject(account.getLogin())
                .setExpiration(expired)
                .signWith(jwtRefreshSecret)
                .compact();
    }


    private Date getExpirationDate(ExpirationTime expirationTime) {
        Instant instant = LocalDateTime.now()
                .plusDays(expirationTime.getDay())
                .plusHours(expirationTime.getHour())
                .plusMinutes(expirationTime.getMinute())
                .plusSeconds(expirationTime.getSecond())
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(instant);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            return false;
        }
        // FIXME: 24.04.2024
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}