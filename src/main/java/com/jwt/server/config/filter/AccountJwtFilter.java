package com.jwt.server.config.filter;

import com.jwt.server.dto.AuthType;
import com.jwt.server.dto.JwtAuthentication;
import com.jwt.server.dto.authorization.Authorization;
import com.jwt.server.exception.SecurityException;
import com.jwt.server.service.SecurityAccountRepository;
import com.jwt.server.tool.JwtHelper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.util.Objects;
import java.util.Optional;

import static com.jwt.server.config.filter.Constant.ACCOUNT_NOT_FOUND;
import static com.jwt.server.config.filter.Constant.ACCOUNT_TYPE;
import static com.jwt.server.config.filter.Constant.HASH_PASSWORD;
import static com.jwt.server.config.filter.Constant.LOGIN;

@Component
@RequiredArgsConstructor
public class AccountJwtFilter extends GenericFilterBean {
    private final JwtHelper jwtHelper;
    private final SecurityAccountRepository securityAccountRepository;

    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        String token = FilterUtil.getTokenFromRequest((HttpServletRequest) servletRequest);
        if (Objects.nonNull(token) && jwtHelper.validateAccessToken(token)) {
            Claims claims = jwtHelper.getAccessClaims(token);

            AuthType type = Optional.of(claims.get(ACCOUNT_TYPE, String.class))
                    .map(AuthType::valueOf)
                    .orElseThrow(() -> new SecurityException("Incorrect token"));

            if (AuthType.COMPANY.equals(type)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            JwtAuthentication jwtAuthentication = jwtHelper.generate(claims);
            String password = claims.get(HASH_PASSWORD, String.class);
            String login = claims.get(LOGIN, String.class);

            Authorization account = this.securityAccountRepository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_FOUND));

            if (FilterUtil.validatePassword(password, account)) {
                jwtAuthentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }
        }
            filterChain.doFilter(servletRequest, servletResponse);



    }

}
