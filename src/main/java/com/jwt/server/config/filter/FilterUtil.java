package com.jwt.server.config.filter;

import com.jwt.server.dto.authorization.Authorization;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.jwt.server.config.filter.Constant.AUTHORIZATION;
import static com.jwt.server.config.filter.Constant.BEARER;

@UtilityClass
public class FilterUtil {

    static String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        return StringUtils.hasText(bearer) && bearer.startsWith(BEARER) ? bearer.substring(7) : null;
    }

    public static String getTokenFromCookie(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .map(List::of)
                .orElse(List.of())
                .stream()
                .filter(cookie -> AUTHORIZATION.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    static boolean validatePassword(String jwtAuthentication, Authorization company) {
        return jwtAuthentication.equals(company.getPassword());
    }

}
