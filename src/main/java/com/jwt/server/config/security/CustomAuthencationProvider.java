package com.jwt.server.config.security;

import com.jwt.server.config.CustomUserDetailsService;
import com.jwt.server.service.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthencationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication)
                                          throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        //получаем пользователя

        UserDetails principal = customUserDetailsService.loadUserByUsername(authentication.getName());
        return new UsernamePasswordAuthenticationToken(
                principal, password, principal.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}