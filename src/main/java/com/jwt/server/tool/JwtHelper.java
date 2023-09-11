package com.jwt.server.tool;

import com.jwt.server.dto.ExpirationTime;
import com.jwt.server.dto.JwtAuthentication;
import com.jwt.server.dto.authorization.Authorization;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.jwt.server.config.filter.Constant.HASH_PASSWORD;
import static com.jwt.server.config.filter.Constant.LOGIN;
import static com.jwt.server.config.filter.Constant.SERVICE;

@RequiredArgsConstructor
public class JwtHelper {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final ExpirationTime accessExpirationTime;
    private final ExpirationTime refreshExpirationTime;

    @Value("${service-name}")
    private String serviceName;

    public String generateAccessToken(Authorization authorization) {
        return Jwts.builder()
                .setSubject(authorization.getLogin())
                .setExpiration(getExpirationDate(accessExpirationTime))
                .signWith(jwtAccessSecret)
                .claim(LOGIN, authorization.getLogin())
                .claim(HASH_PASSWORD, authorization.getPassword())
                .claim(SERVICE, serviceName)
                .compact();
    }

    public String generateRefreshToken(Authorization authorization) {
        return Jwts.builder()
                .setSubject(authorization.getLogin())
                .setExpiration(getExpirationDate(refreshExpirationTime))
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

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

}