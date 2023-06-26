package com.jwt.server.config.filter;

import com.jwt.server.dto.authorization.Authorization;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import static com.jwt.server.config.filter.Constant.AUTHORIZATION;
import static com.jwt.server.config.filter.Constant.BEARER;

@UtilityClass
public class FilterUtil {

    static String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        return StringUtils.hasText(bearer) && bearer.startsWith(BEARER) ? bearer.substring(7) : null;
    }

    static boolean validatePassword(String jwtAuthentication, Authorization company) {
        return jwtAuthentication.equals(company.getPassword());
    }

}
