package com.jwt.service.service;

import com.jwt.service.dto.authorization.Account;

import java.util.Optional;

public interface SecurityAccountService {

    Optional<Account> findByLogin(String login);

}
