package com.jwt.server.config;

import com.jwt.server.dto.authorization.Account;
import com.jwt.server.dto.authorization.Authorization;
import com.jwt.server.service.SecurityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final SecurityAccountRepository securityAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authorization account = securityAccountRepository.findByLogin(username).orElse(null);
        return (Account) account;
    }
}
