package com.jwt.server.service.impl;

import com.jwt.server.dto.authorization.Authorization;
import com.jwt.server.service.AccountRepository;
import com.jwt.server.service.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service(value = "authAccount")
@RequiredArgsConstructor
public class AuthServiceImpl extends AbstractAuthService {
    private final AccountRepository accountRepository;
    private final Cache<Set<String>, String> companyCacheToken;

    @Override
    protected Optional<Authorization> getAccount(String login) {
        return accountRepository.findByLogin(login);
    }

    @Override
    protected Cache<Set<String>, String> getCache() {
        return companyCacheToken;
    }
}
