package com.jwt.server.service.impl;

import com.jwt.server.dto.authorization.Authorization;
import com.jwt.server.service.CompanyRepository;
import com.jwt.server.service.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service(value = "authApiClient")
@RequiredArgsConstructor
public class AuthApiService extends AbstractAuthService {
    private final CompanyRepository companyRepository;
    private final Cache<Set<String>, String> clientCacheToken;

    @Override
    protected Optional<Authorization> getAccount(String login) {
        return companyRepository.findByLogin(login);
    }

    @Override
    protected Cache<Set<String>, String> getCache() {
        return clientCacheToken;
    }

}
