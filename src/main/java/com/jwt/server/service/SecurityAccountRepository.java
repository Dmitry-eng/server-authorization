package com.jwt.server.service;

import com.jwt.server.dto.authorization.Authorization;

import java.util.Optional;

public interface SecurityAccountRepository {

    Optional<Authorization> findByLogin(String login);

}
