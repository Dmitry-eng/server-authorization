package com.jwt.service.util;

import com.jwt.service.dto.authorization.Account;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.jwt.service.util.Constant.ACCESS_TOKEN;
import static com.jwt.service.util.Constant.BEARER;

@UtilityClass
public class FilterUtil {

    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(ACCESS_TOKEN);
        return StringUtils.hasText(bearer) && bearer.startsWith(BEARER) ? bearer.substring(7) : null;
    }

    public static boolean validatePassword(String jwtAuthentication, Account account) {
        return jwtAuthentication.equals(account.getPassword());
    }

    public static String getTokenFromCookie(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .map(List::of)
                .orElse(List.of())
                .stream()
                .filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
