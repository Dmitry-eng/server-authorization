package com.jwt.server.service;

import com.jwt.server.dto.authorization.Authorization;

import java.util.Optional;

public interface CompanyRepository {

    Optional<Authorization> findByLogin(String login);

}

