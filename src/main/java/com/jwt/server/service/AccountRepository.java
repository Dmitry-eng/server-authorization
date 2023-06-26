package com.jwt.server.service;

import com.jwt.server.dto.authorization.Authorization;

import java.util.Optional;

public interface AccountRepository {

    Optional<Authorization> findByLogin(String login);

}
